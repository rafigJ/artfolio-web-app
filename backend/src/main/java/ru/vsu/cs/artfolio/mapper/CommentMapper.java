package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class CommentMapper {

    public static CommentEntity toEntity(CommentRequestDto commentRequest, Long postId, UserEntity user) {
        return CommentEntity.builder()
                .comment(commentRequest.comment())
                .user(user)
                .post(PostEntity.builder().id(postId).build())
                .createTime(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    public static CommentResponseDto toDto(CommentEntity comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .owner(UserMapper.toDto(comment.getUser()))
                .comment(comment.getComment())
                .createTime(comment.getCreateTime())
                .build();
    }

    public static PageDto<CommentResponseDto> toPageDto(Page<CommentEntity> commentPage) {
        List<CommentResponseDto> content = commentPage.getContent().stream().map(CommentMapper::toDto).toList();
        return new PageDto<>(content, commentPage.getTotalElements(), commentPage.getTotalPages());
    }
}

