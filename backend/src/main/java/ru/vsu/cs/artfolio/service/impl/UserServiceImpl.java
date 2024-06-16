package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.CommentEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.mapper.PostMapper;
import ru.vsu.cs.artfolio.mapper.UserMapper;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioRequest;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;
import ru.vsu.cs.artfolio.mapper.wrappers.UserAdditionalInfo;
import ru.vsu.cs.artfolio.repository.CommentRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.FollowService;
import ru.vsu.cs.artfolio.service.LikeService;
import ru.vsu.cs.artfolio.service.UserService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final MinioService minioService;
    private final FollowService followService;
    private final LikeService likeService;
    private final ResourceLoader resourceLoader;

    @Override
    @Transactional
    public FullUserResponseDto updateUserInformation(UserEntity user, UserUpdateRequestDto updatedUser, @Nullable MultipartFile avatar) {
        MinioRequest file;
        try {
            if (avatar != null) {
                file = MinioRequest.of(avatar.getInputStream(), avatar.getContentType());
            } else {
                // если файл null, то берем изображение по default
                InputStream defaultInputStream = resourceLoader.getResource("classpath:default_avatar.png").getInputStream();
                file = MinioRequest.of(defaultInputStream, "image/png");
            }
            minioService.deleteFile(user.getAvatarName());
            MinioResult avatarData = minioService.uploadAvatarFile(file);
            UserEntity updatedEntity = UserMapper.updateEntity(user, updatedUser, avatarData);
            UserAdditionalInfo additionalInfo = getUserAdditionalInfo(user, user);
            return UserMapper.toFullDto(userRepository.save(updatedEntity), additionalInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FullUserResponseDto getUserByUsername(@Nullable UserEntity executor, String username) {
        UserEntity fetchUser = findUserByUsername(username);
        UserAdditionalInfo additionalInfo = getUserAdditionalInfo(executor, fetchUser);

        if (!fetchUser.isDeleted() || (executor != null && executor.isAdmin())) {
            return UserMapper.toFullDto(fetchUser, additionalInfo);
        } else {
            throw new NotFoundException("User by " + username + " username not found");
        }
    }

    @Override
    @Transactional
    public void deleteUser(UserEntity executor, String username) {
        if (!executor.isAdmin()) {
            throw new BadRequestException("Need permits for that");
        }
        // каскадное удаление
        UserEntity userEntity = findUserByUsername(username);
        if (userEntity.isAdmin()) {
            throw new BadRequestException("You can't delete admin");
        }
        userEntity.setDeleted(true);

        List<PostEntity> ownerPosts = postRepository.findAllByOwnerUuid(userEntity.getUuid());
        ownerPosts.forEach(post -> post.setDeleted(true));

        List<CommentEntity> ownerComments = commentRepository.findAllByUserUuid(userEntity.getUuid());
        ownerComments.forEach(comment -> comment.setDeleted(true));

        followService.deleteAllUserSubscribesAndFollowers(userEntity);
        commentRepository.saveAll(ownerComments);
        postRepository.saveAll(ownerPosts);
        userRepository.save(userEntity);
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
        if (followedUser.equals(subscriber)) {
            throw new BadRequestException("User can't subscribe to himself");
        }

        followService.subscribe(subscriber, followedUser);
    }

    @Override
    public void deleteSubscribe(UserEntity subscriber, String followedUsername) {
        UserEntity followedUser = findUserByUsername(followedUsername);

        if (followedUser.equals(subscriber)) {
            throw new BadRequestException("User can't delete/subscribe to himself");
        }

        followService.deleteSubscribe(subscriber, followedUser);
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

    @Override
    public PageDto<PostResponseDto> getPostsPageByUsername(String username, Pageable page) {
        UserEntity user = findUserByUsername(username);
        Page<PostEntity> posts = postRepository.findAllByOwnerUuidAndDeletedIsFalse(user.getUuid(), page);
        List<Long> postIds = posts.getContent().stream().map(PostEntity::getId).toList();
        List<Long> likeCountsEachPostInList = likeService.getLikeCountsEachPostInList(postIds);
        return PostMapper.toPageDto(posts, likeCountsEachPostInList);
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User by " + username + " username not found"));
    }

    @NotNull
    private UserAdditionalInfo getUserAdditionalInfo(@Nullable UserEntity executor, UserEntity fetchUser) {
        List<Long> userPosts = postRepository.findAllByOwnerUuid(fetchUser.getUuid())
                .parallelStream()
                .filter(p -> !p.getDeleted())
                .map(PostEntity::getId)
                .toList();

        Long postCount = (long) userPosts.size();
        Long subscribersCount = followService.countUserFollowers(fetchUser.getUuid());
        Long followingCount = followService.countUserSubscribes(fetchUser.getUuid());
        Long likeCount = likeService.getLikeCount(userPosts);
        Boolean isFollowed = executor != null ? followService.isFollowing(executor.getUuid(), fetchUser.getUuid()) : null;

        return new UserAdditionalInfo(postCount, subscribersCount, followingCount, likeCount, isFollowed);
    }
}

