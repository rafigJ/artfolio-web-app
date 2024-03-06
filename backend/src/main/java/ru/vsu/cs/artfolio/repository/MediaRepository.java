package ru.vsu.cs.artfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;

import java.util.List;

public interface MediaRepository extends JpaRepository<MediaFileEntity, Long> {

    List<MediaFileEntity> findAllByPostIdOrderByPosition(Long postId);

}
