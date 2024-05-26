package ru.vsu.cs.artfolio.service.report;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;

public interface ReportService {

    CommentReportResponseDto createCommentReport(UserEntity sender, Long commentId, ReportRequestDto report);

    CommentReportResponseDto setCommentReportReviewed(UserEntity executor, Long commentReportId, ReportReviewRequestDto reviewDto);

    PageDto<CommentReportResponseDto> getCommentReportsPage(Pageable page);

    PostReportResponseDto createPostReport(UserEntity sender, Long postId, ReportRequestDto report);

    PostReportResponseDto setPostReportReviewed(UserEntity executor, Long postReportId, ReportReviewRequestDto reviewDto);

    PageDto<PostReportResponseDto> getPostReportsPage(Pageable page);

}
