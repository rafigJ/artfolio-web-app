package ru.vsu.cs.artfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.report.CommentReportEntity;

public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

    Page<CommentReportEntity> findAllByReviewed(Boolean review, Pageable pageable);

}
