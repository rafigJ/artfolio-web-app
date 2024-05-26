package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final PostReportRepository reportRepository;

    @Override
    public PostReportResponseDto createPostReport(UserEntity sender, PostEntity post, ReportRequestDto report) {
        PostReportEntity postReport = ReportMapper.postReportToEntity(report, sender, post);
        PostReportEntity savedReport = reportRepository.save(postReport);
        return ReportMapper.postReportToDto(savedReport);
    }

    @Override
    @Transactional
    public PostReportResponseDto setReviewed(UserEntity executor, Long postReportId, ReportReviewRequestDto reviewDto) {
        PostReportEntity postReport = reportRepository.findById(postReportId)
                .orElseThrow(() -> new NotFoundException("Report by " + postReportId + " id not found"));
        postReport.setReviewed(reviewDto.reviewed());
        PostReportEntity updatedReport = reportRepository.save(postReport);
        return ReportMapper.postReportToDto(updatedReport);
    }

    @Override
    public PageDto<PostReportResponseDto> getPostReportsPage(Pageable page) {
        Page<PostReportEntity> reportEntityPage = reportRepository.findAll(page);
        return ReportMapper.postToPageDto(reportEntityPage);
    }
}
