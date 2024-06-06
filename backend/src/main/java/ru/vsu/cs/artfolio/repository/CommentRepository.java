package ru.vsu.cs.artfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.CommentEntity;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPostIdAndDeletedIsFalse(Long postId, Pageable page);

    List<CommentEntity> findAllByUserUuid(UUID userUuid);
}
