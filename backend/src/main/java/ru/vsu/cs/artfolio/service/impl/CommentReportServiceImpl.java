package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.CommentReportEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.mapper.ReportMapper;
import ru.vsu.cs.artfolio.repository.CommentReportRepository;
import ru.vsu.cs.artfolio.service.report.CommentReportService;

@Service
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService {

    private final CommentReportRepository reportRepository;

    @Override
    public CommentReportResponseDto createCommentReport(UserEntity sender, CommentEntity comment, ReportRequestDto report) {
        CommentReportEntity commentReportEntity = ReportMapper.commentReportToEntity(report, sender, comment);
        CommentReportEntity savedReport = reportRepository.save(commentReportEntity);
        return ReportMapper.commentReportToDto(savedReport);
    }

    @Override
    @Transactional
    public CommentReportResponseDto setReviewed(UserEntity executor, Long commentReportId, ReportReviewRequestDto reviewDto) {
        CommentReportEntity commentReport = reportRepository.findById(commentReportId)
                .orElseThrow(() -> new NotFoundException("Report by " + commentReportId + " id not found"));
        commentReport.setReviewed(reviewDto.reviewed());
        CommentReportEntity updatedReport = reportRepository.save(commentReport);
        return ReportMapper.commentReportToDto(updatedReport);
    }

    @Override
    public PageDto<CommentReportResponseDto> getCommentReportsPage(Pageable page) {
        Page<CommentReportEntity> reportEntityPage = reportRepository.findAll(page);
        return ReportMapper.commentToPageDto(reportEntityPage);
    }
}
