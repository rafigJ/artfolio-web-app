package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {

    FullPostResponseDto createPost(UUID userId, PostRequestDto requestDto,
                                   List<MultipartFile> files);

    FullPostResponseDto getPostById(Long id);

    void deletePost(UUID userId, Long id);

    FullPostResponseDto updatePost(UUID userId, Long id, PostRequestDto requestDto, List<MultipartFile> images);

    Page<PostResponseDto> getPostsPageByUserId(UUID userId, Pageable page);

    Page<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page);

    void likePost(UUID userId, Long postId);

    void sendReportToPost(UUID userId, Long postId, String reasonText);

}
