package ru.vsu.cs.artfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.vsu.cs.artfolio.entity.PostEntity;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {

    Optional<PostEntity> findByName(String postName);

    Page<PostEntity> findAllByOwnerUuid(UUID uuid, Pageable pageable);
}
