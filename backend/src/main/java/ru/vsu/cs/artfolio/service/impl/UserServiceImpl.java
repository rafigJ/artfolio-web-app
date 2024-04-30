package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.NotExistUserException;
import ru.vsu.cs.artfolio.mapper.UserMapper;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MinioService minioService;

    @Override
    public FullUserResponseDto updateUserInformation(UUID userId, UserUpdateRequestDto updatedUser, MultipartFile avatar) {
        UserEntity oldUser = userRepository.getReferenceById(userId);
        minioService.deleteFile(oldUser.getAvatarName());
        UserEntity updatedEntity = UserMapper.updateEntity(oldUser, updatedUser, minioService.uploadFile(avatar));
        return UserMapper.toFullDto(userRepository.save(updatedEntity));
    }

    @Override
    public FullUserResponseDto getUserByUsername(String username) {
        return UserMapper.toFullDto(userRepository.findByUsername(username)
                .orElseThrow(NotExistUserException::new));
    }

    @Override
    public void deleteUser(UUID executorId, String username) {
        if (userRepository.getReferenceById(executorId).getRole() == Role.ADMIN) {
            userRepository.removeByUsername(username);
        } else {
            throw new BadRequestException("Need permits for that");
        }
    }

    @Override
    public MediaDto downloadAvatar(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(NotExistUserException::new);
        return new MediaDto(minioService.downloadFile(user.getAvatarName()), user.getAvatarType());
    }

}
