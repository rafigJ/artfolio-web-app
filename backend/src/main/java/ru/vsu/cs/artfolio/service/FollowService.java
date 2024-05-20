package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;

import java.util.UUID;

public interface FollowService {

    void subscribe(UUID subscriberUuid, UUID followedUuid);

    void deleteSubscribe(UUID subscriberUuid, UUID followedUuid);

    /**
     * @param userId - uuid пользователя
     * @return страницу пользователей, на которых подписан пользователь
     */
    PageDto<UserResponseDto> getAllUserSubscribes(UUID userId, Pageable page);

    /**
     * @param userId - uuid пользователя
     * @return страницу пользователей, которые подписаны на пользователя
     */
    PageDto<UserResponseDto> getAllUserFollowers(UUID userId, Pageable page);
}