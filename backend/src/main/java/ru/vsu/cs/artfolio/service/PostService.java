package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {

    FullPostResponseDto createPost(UserEntity executor, PostRequestDto requestDto,
                                   List<MultipartFile> files);

    FullPostResponseDto getPostById(Long id);

    /**
     * Помечает пост удаленным
     */
    void deletePost(UserEntity executor, Long id);

    FullPostResponseDto updatePost(UserEntity executor, Long id, PostRequestDto requestDto, List<MultipartFile> images);

    PageDto<PostResponseDto> getPostsPageByUserId(UUID userId, Pageable page);

    PageDto<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page);

    Long likePost(UUID userId, Long postId);

    Long deleteLikeFromPost(UUID userId, Long postId);

    MediaDto getMediaById(Long mediaId);

    MediaDto getPreviewByPostId(Long postId);
}
