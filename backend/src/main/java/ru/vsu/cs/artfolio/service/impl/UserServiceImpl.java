package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.dto.user.AvatarResponseDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.NotExistUserException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.UserMapper;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public FullUserResponseDto updateUserInformation(UUID userId, UserUpdateRequestDto updatedUser, MultipartFile avatar) {
        try {
            UserEntity userEntity = userRepository.getReferenceById(userId);
            userEntity.setFullName(updatedUser.fullName());
            userEntity.setAdditionalInfo(updatedUser.description());
            userEntity.setCountry(updatedUser.country());
            userEntity.setCity(updatedUser.city());
            userEntity.setUsername(updatedUser.username());
            userEntity.setEmail(updatedUser.email());
            userEntity.setAvatar(avatar.getBytes());
            userEntity.setAvatarType(avatar.getContentType());
            userEntity.setUpdateTime(LocalDateTime.now());
            return UserMapper.toFullDto(userRepository.save(userEntity));
        } catch (IOException e) {
            throw new RestException("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public FullUserResponseDto getUserByUsername(String username) {
        return UserMapper.toFullDto(userRepository.findByUsername(username)
                .orElseThrow(NotExistUserException::new));
    }

    @Override
    @Transactional
    public void deleteUser(UUID executorId, String username) {
        if (userRepository.getReferenceById(executorId).getRole() == Role.ADMIN) {
            userRepository.removeByUsername(username);
        } else {
            throw new BadRequestException("Need permits for that");
        }
    }

    @Override
    @Transactional
    public AvatarResponseDto downloadAvatar(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(NotExistUserException::new);
        return new AvatarResponseDto(userEntity.getAvatar(), userEntity.getAvatarType());
    }

}
