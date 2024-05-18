package ru.vsu.cs.artfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.cs.artfolio.entity.LikeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    boolean existsByUserUuidAndPostId(UUID userId, Long postId);

    Long countByPostId(Long postId);

    Long countByPostIdIn(List<Long> postIds);

    /**
     * Возвращает список массивов, где каждый массив содержит два элемента:
     * идентификатор поста и количество лайков для этого поста.
     *
     * @param postIds Список идентификаторов постов, для которых нужно посчитать количество лайков.
     * @return Список массивов, где каждый массив содержит два элемента: идентификатор поста и количество лайков для этого поста.
     * Если для поста нет лайков, он не будет включен в результат.
     */
    @Query("""
            SELECT l.postId, COUNT(l)
            FROM LikeEntity l
            WHERE l.postId IN :postIds
            GROUP BY l.postId
            """)
    List<Long[]> countEach(List<Long> postIds);

    Optional<LikeEntity> findByUserUuidAndPostId(UUID userId, Long postId);
}
