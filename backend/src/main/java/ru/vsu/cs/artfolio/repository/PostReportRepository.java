package ru.vsu.cs.artfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.PostReportEntity;

public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {
}
