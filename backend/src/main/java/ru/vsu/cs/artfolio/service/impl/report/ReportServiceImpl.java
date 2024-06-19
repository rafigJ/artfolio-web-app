package ru.vsu.cs.artfolio.service.impl.report;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.cs.artfolio.controller.enums.ReportReviewed;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.repository.CommentRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.report.CommentReportService;
import ru.vsu.cs.artfolio.service.report.PostReportService;
import ru.vsu.cs.artfolio.service.report.ReportService;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final PostReportService postReportService;
    private final CommentReportService commentReportService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentReportResponseDto createCommentReport(UserEntity sender, Long commentId, ReportRequestDto report) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment by " + commentId + " not found"));
        return commentReportService.createCommentReport(sender, commentEntity, report);
    }

    @Override
    public CommentReportResponseDto setCommentReportReviewed(UserEntity executor, Long commentReportId, ReportReviewRequestDto reviewDto) {
        if (!executor.isAdmin()) {
            LOG.warn("{} is not admin. Access denied", executor.getUsername());
            throw new RestException("Access denied", HttpStatus.UNAUTHORIZED);
        }
        return commentReportService.setReviewed(executor, commentReportId, reviewDto);
    }

    @Override
    public PageDto<CommentReportResponseDto> getCommentReportsPage(ReportReviewed reportReviewed, Pageable page) {
        return commentReportService.getCommentReportsPage(reportReviewed, page);
    }

    @Override
    public PostReportResponseDto createPostReport(UserEntity sender, Long postId, ReportRequestDto report) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post by " + postId + " not found"));
        return postReportService.createPostReport(sender, postEntity, report);
    }

    @Override
    public PostReportResponseDto setPostReportReviewed(UserEntity executor, Long postReportId, ReportReviewRequestDto reviewDto) {
        if (!executor.isAdmin()) {
            LOG.warn("{} is not admin. Access denied", executor.getUsername());
            throw new RestException("Access denied", HttpStatus.UNAUTHORIZED);
        }
        return postReportService.setReviewed(executor, postReportId, reviewDto);
    }

    @Override
    public PageDto<PostReportResponseDto> getPostReportsPage(ReportReviewed reportReviewed, Pageable page) {
        return postReportService.getPostReportsPage(reportReviewed,page);
    }
}
