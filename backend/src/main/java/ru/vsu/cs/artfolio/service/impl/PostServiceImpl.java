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
import ru.vsu.cs.artfolio.auth.user.Role;
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
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.PostService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private final MinioService minioService;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullPostResponseDto createPost(UUID userId,
                                          PostRequestDto requestDto,
                                          List<MultipartFile> files) {
        LOGGER.info("Получение следующих данных {}, {} для сохранения", userId, requestDto);
        UserEntity ownerEntity = userRepository.getReferenceById(userId);
        if (files.isEmpty()) {
            throw new BadRequestException("Multipart files must have min one file");
        }

        MultipartFile file = files.get(0);
        PostEntity post = PostMapper.toEntity(requestDto, ownerEntity, minioService.uploadPreviewFile(file));

        LOGGER.info("Сохранение поста");
        PostEntity createdPost = postRepository.save(post);
        List<MinioResult> mediaFiles = files.stream().map(minioService::uploadFile).toList();

        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, createdPost));

        LOGGER.info("Возврат ответа");
        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        return PostMapper.toFullDto(createdPost, mediaIds);
    }

    @Override
    public FullPostResponseDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));
        List<Long> mediaIds = postEntity.getMedias().stream().sorted(Comparator.comparingInt(MediaFileEntity::getPosition)).map(MediaFileEntity::getId).toList();
        return PostMapper.toFullDto(postEntity, mediaIds);
    }

    @Override
    public void deletePost(UUID userId, Long id) {
        UserEntity executor = userRepository.getReferenceById(userId);
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));
        if (post.getOwner().getUuid().equals(userId) || executor.getRole() == Role.ADMIN) {
            List<String> fileNames = post.getMedias().stream().map(MediaFileEntity::getFileName).toList();
            minioService.deleteFiles(fileNames);
            postRepository.delete(post);
        } else {
            throw new RestException("Insufficient rights to delete", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullPostResponseDto updatePost(UUID userId, Long id, PostRequestDto requestDto, List<MultipartFile> files) {
        LOGGER.info("Получение следующих данных {}, {}, {} для обновления", userId, id, requestDto);

        PostEntity postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));

        if (!postToUpdate.getOwner().getUuid().equals(userId)) {
            LOGGER.warn("Insufficient rights to update post");
            throw new RestException("Insufficient rights to update post", HttpStatus.UNAUTHORIZED);
        }

        UserEntity ownerEntity = userRepository.getReferenceById(userId);
        MultipartFile file = files.get(0);
        PostEntity newPost = PostMapper.toEntity(requestDto, ownerEntity, minioService.uploadPreviewFile(file));
        newPost.setId(id);

        List<String> fileNames = postToUpdate.getMedias().stream().map(MediaFileEntity::getFileName).toList();
        minioService.deleteFiles(fileNames);
        mediaRepository.deleteAllByPostIdEquals(id);

        PostEntity updatedPost = postRepository.save(newPost);
        List<MinioResult> mediaFiles = files.stream().map(minioService::uploadFile).toList();

        List<MediaFileEntity> medias = mediaRepository.saveAll(MediaMapper.toEntityList(mediaFiles, updatedPost));
        List<Long> mediaIds = medias.stream().map(MediaFileEntity::getId).toList();
        return PostMapper.toFullDto(updatedPost, mediaIds);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageByUserId(UUID userId, Pageable page) {
        Page<PostEntity> posts = postRepository.findAllByOwnerUuid(userId, page);
        return PostMapper.toPageDto(posts);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page) {
        Page<PostEntity> posts = postRepository.findAll(specification, page);
        return PostMapper.toPageDto(posts);
    }

    @Override
    public void likePost(UUID userId, Long postId) {
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendReportToPost(UUID userId, Long postId, String reasonText) {
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public MediaDto getMediaById(Long mediaId) {
        MediaFileEntity media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new NotFoundException("Media by id: " + mediaId + " not found"));
        return new MediaDto(minioService.downloadFile(media.getFileName()), media.getType());
    }

    @Override
    public MediaDto getPreviewByPostId(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post by id: " + postId + " not found"));
        return new MediaDto(minioService.downloadFile(post.getPreviewMediaName()), post.getPreviewType());
    }
}
