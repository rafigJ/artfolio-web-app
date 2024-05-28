package ru.vsu.cs.artfolio.service.report;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.controller.enums.ReportReviewed;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

public interface CommentReportService {

    CommentReportResponseDto createCommentReport(UserEntity sender, CommentEntity comment, ReportRequestDto report);

    CommentReportResponseDto setReviewed(UserEntity executor, Long commentReportId, ReportReviewRequestDto reviewDto);

    PageDto<CommentReportResponseDto> getCommentReportsPage(ReportReviewed reportReviewed, Pageable page);

}
