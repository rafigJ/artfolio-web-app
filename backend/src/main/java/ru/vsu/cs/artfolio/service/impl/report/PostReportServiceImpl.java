package ru.vsu.cs.artfolio.service.impl.report;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.controller.enums.ReportReviewed;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.PostReportEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.mapper.ReportMapper;
import ru.vsu.cs.artfolio.repository.PostReportRepository;
import ru.vsu.cs.artfolio.service.report.PostReportService;

@Service
@RequiredArgsConstructor
public class PostReportServiceImpl implements PostReportService {

    private final PostReportRepository repository;

    @Override
    public PostReportResponseDto createPostReport(UserEntity sender, PostEntity post, ReportRequestDto report) {
        PostReportEntity postReport = ReportMapper.postReportToEntity(report, sender, post);
        PostReportEntity savedReport = repository.save(postReport);
        return ReportMapper.postReportToDto(savedReport);
    }

    @Override
    @Transactional
    public PostReportResponseDto setReviewed(UserEntity executor, Long postReportId, ReportReviewRequestDto reviewDto) {
        PostReportEntity postReport = repository.findById(postReportId)
                .orElseThrow(() -> new NotFoundException("Report by " + postReportId + " id not found"));
        postReport.setReviewed(reviewDto.reviewed());
        PostReportEntity updatedReport = repository.save(postReport);
        return ReportMapper.postReportToDto(updatedReport);
    }

    @Override
    public PageDto<PostReportResponseDto> getPostReportsPage(ReportReviewed reportReviewed, Pageable page) {
        Page<PostReportEntity> reportEntityPage = switch (reportReviewed) {
            case ALL -> repository.findAll(page);
            case TRUE -> repository.findAllByReviewed(true, page);
            case FALSE -> repository.findAllByReviewed(false, page);
        };
        return ReportMapper.postToPageDto(reportEntityPage);
    }
}
