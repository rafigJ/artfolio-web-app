package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.MediaMapper;
import ru.vsu.cs.artfolio.mapper.PostMapper;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.LikeService;
import ru.vsu.cs.artfolio.service.PostService;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);

    private final MinioService minioService;
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;

    @Override
    @Transactional
    public FullPostResponseDto createPost(UserEntity executor, PostRequestDto requestDto, List<MultipartFile> files) {
        LOG.info("Creating post with data: {}, {}", executor.getUuid(), requestDto);
        validateFiles(files);

        MultipartFile file = files.get(0);
        PostEntity post = PostMapper.toEntity(requestDto, executor, minioService.uploadPreviewFile(file));

        PostEntity createdPost = postRepository.save(post);
        List<MinioResult> mediaFiles = uploadFiles(files);

        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, createdPost));

        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        return PostMapper.toFullDto(createdPost, mediaIds, 0L, false);
    }

    private static void validateFiles(List<MultipartFile> files) {
        if (files.isEmpty()) {
            LOG.warn("Multipart files must have at least one file");
            throw new BadRequestException("Multipart files must have at least one file");
        }
        if (files.size() > 10) {
            LOG.warn("File list must be less than 10");
            throw new BadRequestException("File list must be less than 10");
        }
    }

    @Override
    public FullPostResponseDto getPostById(@Nullable UserEntity user, Long id) {
        PostEntity postEntity = (user != null && user.isAdmin()) ? findPostById(id) : findNonDeletedPostById(id);

        List<Long> mediaIds = postEntity.getMedias().stream()
                .sorted(Comparator.comparingInt(MediaFileEntity::getPosition))
                .map(MediaFileEntity::getId).toList();

        Boolean hasLike = (user != null) ? likeService.hasLike(user.getUuid(), id) : null;
        return PostMapper.toFullDto(postEntity, mediaIds, likeService.getLikeCount(id), hasLike);
    }

    private PostEntity findNonDeletedPostById(Long id) {
        return postRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));
    }

    @Override
    public void deletePost(UserEntity executor, Long id) {
        PostEntity post = findPostById(id);
        if (isAuthorizedToDelete(executor, post)) {
            post.setDeleted(true);
            postRepository.save(post);
        } else {
            throw new RestException("Access denied", HttpStatus.UNAUTHORIZED);
        }
    }

    private static boolean isAuthorizedToDelete(UserEntity executor, PostEntity post) {
        return post.getOwner().equals(executor) || executor.isAdmin();
    }

    @Override
    @Transactional
    public FullPostResponseDto updatePost(UserEntity executor, Long id, PostRequestDto requestDto, List<MultipartFile> files) {
        LOG.info("Updating post with data: {}, {}, {}", executor.getUuid(), id, requestDto);

        PostEntity postToUpdate = findPostById(id);
        if (!postToUpdate.getOwner().equals(executor)) {
            LOG.warn("Access denied");
            throw new RestException("Access denied", HttpStatus.UNAUTHORIZED);
        }

        updatePostDetails(postToUpdate, requestDto, files);

        postRepository.save(postToUpdate);

        List<MinioResult> mediaFiles = uploadFiles(files);

        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, postToUpdate));

        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        Boolean hasLike = likeService.hasLike(executor.getUuid(), id);
        return PostMapper.toFullDto(postToUpdate, mediaIds, likeService.getLikeCount(id), hasLike);
    }

    @NotNull
    private List<MinioResult> uploadFiles(List<MultipartFile> files) {
        Map<String, MultipartFile> nameFile = new HashMap<>();
        Map<String, Integer> namePosition = new HashMap<>();
        int i = 0;
        for (MultipartFile multipartFile : files) {
            String name = UUID.randomUUID().toString();
            namePosition.put(name, i++);
            nameFile.put(name, multipartFile);
        }
        return nameFile.entrySet().parallelStream()
                .map(v -> minioService.uploadFile(v.getValue(), v.getKey()))
                .sorted(Comparator.comparing(a -> namePosition.get(a.name())))
                .toList();
    }

    private void updatePostDetails(PostEntity postToUpdate, PostRequestDto requestDto, List<MultipartFile> files) {
        MultipartFile file = files.get(0);
        MinioResult previewMedia = minioService.uploadPreviewFile(file);
        postToUpdate.setName(requestDto.getName());
        postToUpdate.setDescription(requestDto.getDescription());
        postToUpdate.setPreviewType(previewMedia.contentType());
        postToUpdate.setPreviewMediaName(previewMedia.name());

        deleteExistingMedias(postToUpdate);
    }

    private void deleteExistingMedias(PostEntity postToUpdate) {
        List<String> fileNames = postToUpdate.getMedias().stream().map(MediaFileEntity::getFileName).toList();
        minioService.deleteFiles(fileNames);
        mediaRepository.deleteAllByPostIdEquals(postToUpdate.getId());
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page) {
        Page<PostEntity> posts = postRepository.findAll(specification, page);
        List<Long> postIds = posts.getContent().stream().map(PostEntity::getId).toList();
        List<Long> likeCountsEachPostInList = likeService.getLikeCountsEachPostInList(postIds);
        return PostMapper.toPageDto(posts, likeCountsEachPostInList);
    }

    @Override
    public Long likePost(UUID userId, Long postId) {
        verifyPostExists(postId);
        likeService.createLike(userId, postId);
        return likeService.getLikeCount(postId);
    }

    @Override
    public Long deleteLikeFromPost(UUID userId, Long postId) {
        verifyPostExists(postId);
        likeService.deleteLike(userId, postId);
        return likeService.getLikeCount(postId);
    }

    private void verifyPostExists(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post by id: " + postId + " not found");
        }
    }

    @Override
    public MediaDto getMediaById(Long mediaId) {
        MediaFileEntity media = findMediaById(mediaId);
        return new MediaDto(minioService.downloadFile(media.getFileName()), media.getType());
    }

    @Override
    public MediaDto getPreviewByPostId(Long postId) {
        PostEntity post = findPostById(postId);
        return new MediaDto(minioService.downloadFile(post.getPreviewMediaName()), post.getPreviewType());
    }

    private MediaFileEntity findMediaById(Long mediaId) {
        return mediaRepository.findById(mediaId)
                .orElseThrow(() -> new NotFoundException("Media by id: " + mediaId + " not found"));
    }

    private PostEntity findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post by id: " + postId + " not found"));
    }

}
