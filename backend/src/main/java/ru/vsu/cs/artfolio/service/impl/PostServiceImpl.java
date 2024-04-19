package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.PostMapper;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.MediaService;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullPostResponseDto createPost(UUID userId,
                                          PostRequestDto requestDto,
                                          List<MultipartFile> files) {

        LOGGER.info("Получение следующих данных {}, {}, {}", userId, requestDto, files);
        UserEntity ownerEntity = userRepository.getReferenceById(userId);

        try {
            PostEntity post = PostMapper.toEntity(requestDto, ownerEntity, files);
            LOGGER.info("Сохранение поста");
            PostEntity createdPost = postRepository.save(post);
            LOGGER.info("Возврат ответа");
            return PostMapper.toFullDto(createdPost);
        } catch (IOException e) {
            LOGGER.error("ОШИБКА {}", e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public FullPostResponseDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));

        return PostMapper.toFullDto(postEntity);
    }

    @Override
    public void deletePost(UUID userId, Long id) {
        UserEntity executor = userRepository.getReferenceById(userId);
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));
        if (post.getOwner().getUuid().equals(userId) || executor.getRole() == Role.ADMIN) {
            postRepository.delete(post);
        } else {
            throw new RestException("Insufficient rights to delete", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public FullPostResponseDto updatePost(UUID userId, Long id, PostRequestDto requestDto, List<MultipartFile> files) {
        LOGGER.info("Получение следующих данных {}, {}, {}", userId, requestDto, files);
        PostEntity postToUpdate = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post by id: " + id + " not found"));

        if (!postToUpdate.getOwner().getUuid().equals(userId)) {
            throw new RestException("Insufficient rights to update post", HttpStatus.UNAUTHORIZED);
        }

        UserEntity ownerEntity = userRepository.getReferenceById(userId);

        try {
            PostEntity newPost = PostMapper.toEntity(requestDto, ownerEntity, files);
            newPost.setId(id);

            LOGGER.info("Обновление поста");
            PostEntity updatedPost = postRepository.save(newPost);
            LOGGER.info("Возврат ответа");
            return PostMapper.toFullDto(updatedPost);
        } catch (IOException e) {
            LOGGER.error("ОШИБКА {}", e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public MediaFileEntity getMediaById(Long mediaId) {
        return mediaService.downloadMedia(mediaId);
    }
}
