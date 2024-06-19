package ru.vsu.cs.artfolio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.cs.artfolio.entity.FollowEntity;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    boolean existsBySubscriber_UuidAndFollowed_Uuid(UUID subscriberUuid, UUID followedUuid);

    Optional<FollowEntity> findBySubscriber_UuidAndFollowed_Uuid(UUID subscriberUuid, UUID followedUuid);

    /**
     * Для получения подписок пользователей, которые подписаны на username
     */
    Page<FollowEntity> findAllByFollowedUuid(UUID uuid, Pageable page);

    Long countByFollowedUuid(UUID uuid);

    /**
     * Для получения подписок пользователя с uuid (на которых он сам подписался)
     */
    Page<FollowEntity> findAllBySubscriberUuid(UUID uuid, Pageable page);

    Long countBySubscriberUuid(UUID uuid);

    /**
     * Для удаления всех сущностей, связанных с конкретным пользователем
     */
    void deleteAllByFollowedUuidOrSubscriberUuid(UUID followId, UUID subscriberId);
}
