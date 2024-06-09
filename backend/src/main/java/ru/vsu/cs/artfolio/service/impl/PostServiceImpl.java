package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private final MinioService minioService;
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullPostResponseDto createPost(UserEntity executor, PostRequestDto requestDto, List<MultipartFile> files) {
        LOGGER.info("Creating post with data: {}, {}", executor.getUuid(), requestDto);
        if (files.isEmpty()) {
            throw new BadRequestException("Multipart files must have at least one file");
        }

        MultipartFile file = files.get(0);
        PostEntity post = PostMapper.toEntity(requestDto, executor, minioService.uploadPreviewFile(file));

        LOGGER.info("Saving post");
        PostEntity createdPost = postRepository.save(post);
        List<MinioResult> mediaFiles = files.stream().map(minioService::uploadFile).toList();
        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, createdPost));

        LOGGER.info("Returning response");
        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        return PostMapper.toFullDto(createdPost, mediaIds, 0L, false);
    }

    @Override
    public FullPostResponseDto getPostById(@Nullable UserEntity user, Long id) {
        PostEntity postEntity;
        if (user != null && user.isAdmin()) {
            postEntity = findPostById(id);
        } else {
            postEntity = postRepository.findById(id)
                    .filter(p -> !p.getDeleted())
                    .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));
        }

        List<Long> mediaIds = postEntity.getMedias().stream()
                .sorted(Comparator.comparingInt(MediaFileEntity::getPosition))
                .map(MediaFileEntity::getId).toList();

        Boolean hasLike = (user != null) ? likeService.hasLike(user.getUuid(), id) : null;
        return PostMapper.toFullDto(postEntity, mediaIds, likeService.getLikeCount(id), hasLike);
    }

    @Override
    public void deletePost(UserEntity executor, Long id) {
        PostEntity post = findPostById(id);
        if (post.getOwner().equals(executor) || executor.isAdmin()) {
            post.setDeleted(true);
            postRepository.save(post);
        } else {
            throw new RestException("Insufficient rights to delete", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullPostResponseDto updatePost(UserEntity executor, Long id, PostRequestDto requestDto, List<MultipartFile> files) {
        LOGGER.info("Updating post with data: {}, {}, {}", executor.getUuid(), id, requestDto);

        PostEntity postToUpdate = findPostById(id);
        if (!postToUpdate.getOwner().equals(executor)) {
            LOGGER.warn("Insufficient rights to update post");
            throw new RestException("Insufficient rights to update post", HttpStatus.UNAUTHORIZED);
        }

        MultipartFile file = files.get(0);
        PostEntity newPost = PostMapper.toEntity(requestDto, executor, minioService.uploadPreviewFile(file));
        newPost.setId(id);

        List<String> fileNames = postToUpdate.getMedias().stream().map(MediaFileEntity::getFileName).toList();
        minioService.deleteFiles(fileNames);
        mediaRepository.deleteAllByPostIdEquals(id);

        PostEntity updatedPost = postRepository.save(newPost);
        List<MinioResult> mediaFiles = files.stream().map(minioService::uploadFile).toList();
        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, updatedPost));

        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        Boolean hasLike = likeService.hasLike(executor.getUuid(), id);
        return PostMapper.toFullDto(updatedPost, mediaIds, likeService.getLikeCount(id), hasLike);
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
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post by id: " + postId + " not found");
        }
        likeService.createLike(userId, postId);
        return likeService.getLikeCount(postId);
    }

    @Override
    public Long deleteLikeFromPost(UUID userId, Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post by id: " + postId + " not found");
        }
        likeService.deleteLike(userId, postId);
        return likeService.getLikeCount(postId);
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
