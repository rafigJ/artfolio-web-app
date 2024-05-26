package ru.vsu.cs.artfolio.service.report;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

public interface PostReportService {

    PostReportResponseDto createPostReport(UserEntity sender, PostEntity post, ReportRequestDto report);

    PostReportResponseDto setReviewed(UserEntity executor, Long postReportId, ReportReviewRequestDto reviewDto);

    PageDto<PostReportResponseDto> getPostReportsPage(Pageable page);

}
