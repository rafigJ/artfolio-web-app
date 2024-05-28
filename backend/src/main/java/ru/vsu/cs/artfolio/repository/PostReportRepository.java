package ru.vsu.cs.artfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.PostReportEntity;

public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {

    Page<PostReportEntity> findAllByReviewed(Boolean review, Pageable pageable);

}
