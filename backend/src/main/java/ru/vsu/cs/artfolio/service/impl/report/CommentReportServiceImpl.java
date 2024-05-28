package ru.vsu.cs.artfolio.service.impl.report;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.controller.enums.ReportReviewed;
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

    private final CommentReportRepository repository;

    @Override
    public CommentReportResponseDto createCommentReport(UserEntity sender, CommentEntity comment, ReportRequestDto report) {
        CommentReportEntity commentReportEntity = ReportMapper.commentReportToEntity(report, sender, comment);
        CommentReportEntity savedReport = repository.save(commentReportEntity);
        return ReportMapper.commentReportToDto(savedReport);
    }

    @Override
    @Transactional
    public CommentReportResponseDto setReviewed(UserEntity executor, Long commentReportId, ReportReviewRequestDto reviewDto) {
        CommentReportEntity commentReport = repository.findById(commentReportId)
                .orElseThrow(() -> new NotFoundException("Report by " + commentReportId + " id not found"));
        commentReport.setReviewed(reviewDto.reviewed());
        CommentReportEntity updatedReport = repository.save(commentReport);
        return ReportMapper.commentReportToDto(updatedReport);
    }

    @Override
    public PageDto<CommentReportResponseDto> getCommentReportsPage(ReportReviewed reportReviewed, Pageable page) {
        Page<CommentReportEntity> reportEntityPage = switch (reportReviewed) {
            case ALL -> repository.findAll(page);
            case TRUE -> repository.findAllByReviewed(true, page);
            case FALSE -> repository.findAllByReviewed(false, page);
        };
        return ReportMapper.commentToPageDto(reportEntityPage);
    }
}
