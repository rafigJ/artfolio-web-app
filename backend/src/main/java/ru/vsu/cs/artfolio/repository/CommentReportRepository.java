package ru.vsu.cs.artfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.CommentReportEntity;

public interface CommentReportRepository extends JpaRepository<CommentReportEntity, Long> {

}
