package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.NotExistUserException;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.mapper.UserMapper;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.FollowService;
import ru.vsu.cs.artfolio.service.UserService;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static ru.vsu.cs.artfolio.criteria.PostSpecifications.byOwnerId;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final MinioService minioService;
    private final FollowService followService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FullUserResponseDto updateUserInformation(UUID userId, UserUpdateRequestDto updatedUser, MultipartFile avatar) {
        UserEntity oldUser = userRepository.getReferenceById(userId);
        minioService.deleteFile(oldUser.getAvatarName());
        UserEntity updatedEntity = UserMapper.updateEntity(oldUser, updatedUser, minioService.uploadFile(avatar));
        return UserMapper.toFullDto(userRepository.save(updatedEntity));
    }

    @Override
    public FullUserResponseDto getUserByUsername(@Nullable UserEntity executor, String username) {
        UserEntity fetchUser = findUserByUsername(username);
        if (!fetchUser.isDeleted()) {
            return UserMapper.toFullDto(fetchUser);
        }
        if (executor != null && (executor.isAdmin() || executor.getUuid().equals(fetchUser.getUuid()))) {
            // В случае если executor админ или получает свои данные
            return UserMapper.toFullDto(fetchUser);
        } else {
            throw new NotExistUserException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(UserEntity executor, String username) {
        if (executor.isAdmin()) {
            UserEntity userEntity = findUserByUsername(username);
            userEntity.setDeleted(true);
            List<PostEntity> ownerPosts = postRepository.findAll(byOwnerId(userEntity.getUuid()));
            for (PostEntity ownerPost : ownerPosts) {
                ownerPost.setDeleted(true);
            }
            postRepository.saveAll(ownerPosts);
            userRepository.save(userEntity);
        } else {
            throw new BadRequestException("Need permits for that");
        }
    }

    @Override
    public MediaDto downloadAvatar(String username) {
        UserEntity user = findUserByUsername(username);
        return new MediaDto(minioService.downloadFile(user.getAvatarName()), user.getAvatarType());
    }

    @Override
    public void subscribe(UserEntity subscriber, String followedUsername) {
        UserEntity followedUser = findUserByUsername(followedUsername);
        if (followedUser.isDeleted()) {
            throw new BadRequestException("Can't subscribe to deleted user");
        }
        if (followedUser.getUuid().equals(subscriber.getUuid())) {
            throw new BadRequestException("User can't subscribe to himself");
        }
        followService.subscribe(subscriber, followedUser);
    }

    @Override
    public void deleteSubscribe(UUID subscriberUuid, String followedUsername) {
        UserEntity followedUser = findUserByUsername(followedUsername);
        if (followedUser.getUuid().equals(subscriberUuid)) {
            throw new BadRequestException("User can't delete/subscribe to himself");
        }
        followService.deleteSubscribe(subscriberUuid, followedUser.getUuid());
    }

    @Override
    public PageDto<UserResponseDto> getAllUserSubscribes(String username, Pageable page) {
        UserEntity user = findUserByUsername(username);
        return followService.getAllUserSubscribes(user.getUuid(), page);
    }

    @Override
    public PageDto<UserResponseDto> getAllUserFollowers(String username, Pageable page) {
        UserEntity user = findUserByUsername(username);
        return followService.getAllUserFollowers(user.getUuid(), page);
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User by " + username + " username not found"));
    }
}
