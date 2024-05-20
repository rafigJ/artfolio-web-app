package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;

import java.util.UUID;

public interface UserService {

    FullUserResponseDto updateUserInformation(UUID userId, UserUpdateRequestDto updatedUser, MultipartFile avatar);

    FullUserResponseDto getUserByUsername(String username);

    void deleteUser(UUID executorId, String username);

    MediaDto downloadAvatar(String username);

    void subscribe(UUID subscriberUuid, String followedUsername);

    void deleteSubscribe(UUID subscriberUuid, String followedUsername);

    /**
     * @param username - username пользователя
     * @return страницу пользователей, на которых подписан пользователь
     */
    PageDto<UserResponseDto> getAllUserSubscribes(String username, Pageable page);

    /**
     * @param username - username пользователя
     * @return страницу пользователей, которые подписаны на пользователя
     */
    PageDto<UserResponseDto> getAllUserFollowers(String username, Pageable page);
}
