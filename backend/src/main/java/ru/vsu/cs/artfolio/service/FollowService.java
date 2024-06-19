package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.util.UUID;

public interface FollowService {

    void subscribe(UserEntity subscriber, UserEntity followed);

    void deleteSubscribe(UserEntity subscriber, UserEntity followed);

    /**
     * Для удаление всех FollowEntity связанных с данным пользователем
     * Используется для каскадного удаления в userService
     *
     * @param user пользователь
     */
    void deleteAllUserSubscribesAndFollowers(UserEntity user);

    /**
     * @param userId uuid пользователя
     * @return страницу пользователей, на которых подписан пользователь
     */
    PageDto<UserResponseDto> getAllUserSubscribes(UUID userId, Pageable page);

    /**
     * @param userId uuid пользователя
     * @return количество пользователей, на которых подписан пользователь
     */
    Long countUserSubscribes(UUID userId);

    /**
     * @param userId uuid пользователя
     * @return страницу пользователей, которые подписаны на пользователя
     */
    PageDto<UserResponseDto> getAllUserFollowers(UUID userId, Pageable page);

    /**
     * @param userId uuid пользователя
     * @return количество пользователей, которые подписаны на пользователя
     */
    Long countUserFollowers(UUID userId);

    /**
     * Возвращает флаг о том подписан ли пользователь на пользователя
     *
     * @return true если пользователь подписан, false иначе
     */
    boolean isFollowing(UUID subscriberId, UUID followedId);
}
