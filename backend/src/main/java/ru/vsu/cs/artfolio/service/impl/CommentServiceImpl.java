package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.CommentMapper;
import ru.vsu.cs.artfolio.repository.CommentRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public PageDto<CommentResponseDto> getAllByPostId(Long postId, Pageable page) {
        Page<CommentEntity> comments = commentRepository.findAllByPostIdAndDeletedIsFalse(postId, page);
        return CommentMapper.toPageDto(comments);
    }

    @Override
    public CommentResponseDto createComment(UserEntity user, Long postId, CommentRequestDto requestDto) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post by " + postId + " id not found");
        }
        CommentEntity comment = CommentMapper.toEntity(requestDto, postId, user);
        comment = commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteCommentById(UserEntity user, Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by " + commentId + " not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new RestException("Comment by " + commentId + " doesn't belong to post by " + postId, HttpStatus.BAD_REQUEST);
        }

        if (comment.getUser().getUuid().equals(user.getUuid()) || user.getRole().equals(Role.ADMIN)) {
            comment.setDeleted(true);
            commentRepository.save(comment);
        } else {
            throw new RestException("Insufficient rights to delete comment", HttpStatus.UNAUTHORIZED);
        }
    }

}
