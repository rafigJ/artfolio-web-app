package ru.vsu.cs.artfolio.service;

import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.user.AvatarResponseDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;

import java.util.UUID;

public interface UserService {

    FullUserResponseDto updateUserInformation(UUID userId, UserUpdateRequestDto updatedUser, MultipartFile avatar);

    FullUserResponseDto getUserByUsername(String username);

    void deleteUser(UUID executorId, String username);

    MediaDto downloadAvatar(String username);

}
