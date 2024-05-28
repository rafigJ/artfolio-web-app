package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.report.CommentReportEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.report.PostReportEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ReportMapper {

    public static CommentReportEntity commentReportToEntity(ReportRequestDto requestDto, UserEntity sender, CommentEntity comment) {
        return CommentReportEntity.builder()
                .comment(comment)
                .sender(sender)
                .reason(requestDto.reason())
                .reviewed(false)
                .createTime(LocalDateTime.now())
                .build();
}

    public static CommentReportResponseDto commentReportToDto(CommentReportEntity report) {
        CommentEntity targetComment = report.getComment();
        return CommentReportResponseDto.builder()
                .id(report.getId())
                .postId(targetComment.getPost().getId())
                .commentId(targetComment.getId())
                .comment(targetComment.getComment())
                .reviewed(report.getReviewed())
                .createTime(report.getCreateTime())
                .targetUser(UserMapper.toDto(targetComment.getUser()))
                .sender(UserMapper.toDto(report.getSender()))
                .build();
    }



    public static PostReportEntity postReportToEntity(ReportRequestDto requestDto, UserEntity sender, PostEntity post) {
        return PostReportEntity.builder()
                .post(post)
                .sender(sender)
                .reason(requestDto.reason())
                .reviewed(false)
                .createTime(LocalDateTime.now())
                .build();
    }

    public static PostReportResponseDto postReportToDto(PostReportEntity report) {
        PostEntity targetPost = report.getPost();
        return PostReportResponseDto.builder()
                .id(report.getId())
                .postId(targetPost.getId())
                .reviewed(report.getReviewed())
                .createTime(report.getCreateTime())
                .targetUser(UserMapper.toDto(targetPost.getOwner()))
                .sender(UserMapper.toDto(report.getSender()))
                .build();
    }

    public static PageDto<CommentReportResponseDto> commentToPageDto(Page<CommentReportEntity> commentReportPage) {
        List<CommentReportResponseDto> commentDtoList = commentReportPage.getContent().stream().map(ReportMapper::commentReportToDto).toList();
        return new PageDto<>(commentDtoList, commentReportPage.getTotalElements(), commentReportPage.getTotalPages());
    }

    public static PageDto<PostReportResponseDto> postToPageDto(Page<PostReportEntity> postReportPage) {
        List<PostReportResponseDto> postReport = postReportPage.getContent().stream().map(ReportMapper::postReportToDto).toList();
        return new PageDto<>(postReport, postReportPage.getTotalElements(), postReportPage.getTotalPages());
    }
}
