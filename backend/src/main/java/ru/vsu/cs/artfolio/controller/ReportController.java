package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.controller.enums.ReportReviewed;
import ru.vsu.cs.artfolio.controller.enums.ReportType;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.report.CommentReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.PostReportResponseDto;
import ru.vsu.cs.artfolio.dto.report.ReportRequestDto;
import ru.vsu.cs.artfolio.dto.report.ReportReviewRequestDto;
import ru.vsu.cs.artfolio.service.report.ReportService;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    @GetMapping()
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    // todo Нужно подавать страницы в зависимости от enum (ReportReviewed, Type)
    public ResponseEntity<PageDto<?>> getPostsPage(
            @RequestParam(value = "type") ReportType type,
            @RequestParam(value = "reviewed", defaultValue = "ALL") ReportReviewed reportReviewed,
            @RequestParam(value = "_page") Integer page,
            @RequestParam(value = "_limit", defaultValue = "10") Integer limit) {
        Pageable pageable = PageRequest.of(page, limit);
        PageDto<?> result = switch (type) {
            case POST -> service.getPostReportsPage(pageable);
            case COMMENT -> service.getCommentReportsPage(pageable);
        };
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/posts/{reportId}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    public ResponseEntity<PostReportResponseDto> setPostReviewed(@PathVariable("reportId") Long reportId,
                                                                 @RequestBody @Valid ReportReviewRequestDto reviewDto,
                                                                 @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.setPostReportReviewed(user.getUserEntity(), reportId, reviewDto));
    }

    @PatchMapping("/comments/{reportId}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    public ResponseEntity<CommentReportResponseDto> setCommentReviewed(@PathVariable("reportId") Long reportId,
                                                                       @RequestBody @Valid ReportReviewRequestDto reviewDto,
                                                                       @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.setCommentReportReviewed(user.getUserEntity(), reportId, reviewDto));
    }

    @PostMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostReportResponseDto> sendPostReport(@PathVariable("postId") Long id,
                                                                @RequestBody @Valid ReportRequestDto reportRequestDto,
                                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.createPostReport(user.getUserEntity(), id, reportRequestDto));
    }

    @PostMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentReportResponseDto> sendCommentReport(@PathVariable("commentId") Long id,
                                                                      @RequestBody @Valid ReportRequestDto reportRequestDto,
                                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.createCommentReport(user.getUserEntity(), id, reportRequestDto));
    }
}
