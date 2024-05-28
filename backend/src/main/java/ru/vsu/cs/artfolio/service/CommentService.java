package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;

public interface CommentService {

    PageDto<CommentResponseDto> getAllByPostId(Long postId, Pageable page);

    CommentResponseDto createComment(UserEntity user, Long postId, CommentRequestDto requestDto);

    /**
     * Помечает пост удаленным
     */
    void deleteCommentById(UserEntity user, Long postId, Long commentId);

}
