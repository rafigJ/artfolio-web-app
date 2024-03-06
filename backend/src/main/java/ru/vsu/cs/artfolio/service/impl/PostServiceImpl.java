package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.UserResponseDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.MediaService;
import ru.vsu.cs.artfolio.service.PostService;

import java.time.LocalDateTime;
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

        PostEntity post = PostEntity.builder()
                .name(requestDto.name())
                .createTime(LocalDateTime.now())
                .owner(ownerEntity)
                .build();

        try {
            LOGGER.info("Сохранение поста");
            PostEntity createdPost = postRepository.saveAndFlush(post);
            LOGGER.info("Загрузка медиа");
            List<Long> mediaIds = mediaService.uploadMedia(createdPost.getId(), files);

            FullPostResponseDto mappedPost = FullPostResponseDto.builder()
                    .id(createdPost.getId())
                    .mediaIds(mediaIds)
                    .name(requestDto.name())
                    .owner(modelMapper.map(createdPost.getOwner(), UserResponseDto.class))
                    .description(requestDto.description())
                    .build();
            LOGGER.info("Возврат ответа");
            return mappedPost;
        } catch (Exception e) {
            LOGGER.error("ОШИБКА {}", e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public FullPostResponseDto getPostById(Long id) {
        // TODO implement
        return null;
    }

    @Override
    public void deletePost(UUID userId, Long id) {
        // TODO implement
    }

    @Override
    public FullPostResponseDto updatePost(UUID userId, Long id, PostRequestDto requestDto, List<MultipartFile> images) {
        // TODO implement
        return null;
    }

    @Override
    public Page<PostResponseDto> getPostsPageByUserId(UUID userId, Pageable page) {
        // TODO implement
        return null;
    }

    @Override
    public Page<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page) {
        // TODO implement
        return null;
    }

    @Override
    public void likePost(UUID userId, Long postId) {
        // TODO implement
    }

    @Override
    public void sendReportToPost(UUID userId, Long postId, String reasonText) {
        // TODO implement
    }
}
