package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.FollowEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.mapper.UserMapper;
import ru.vsu.cs.artfolio.repository.FollowRepository;
import ru.vsu.cs.artfolio.service.FollowService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository repository;

    @Override
    public void subscribe(UserEntity subscriber, UserEntity followed) {
        if (repository.existsBySubscriber_UuidAndFollowed_Uuid(subscriber.getUuid(), followed.getUuid())) {
            return;
        }
        var followEntity = new FollowEntity(null, subscriber, followed);
        repository.save(followEntity);
    }

    @Override
    public void deleteSubscribe(UserEntity subscriber, UserEntity followed) {
        Optional<FollowEntity> followEntity = repository.findBySubscriber_UuidAndFollowed_Uuid(subscriber.getUuid(), followed.getUuid());
        if (followEntity.isEmpty()) {
            return;
        }
        repository.delete(followEntity.get());
    }

    @Override
    public PageDto<UserResponseDto> getAllUserSubscribes(UUID userId, Pageable page) {
        Page<UserEntity> subscribes = repository.findAllBySubscriberUuid(userId, page).map(FollowEntity::getFollowed);
        return UserMapper.toPageDto(subscribes);
    }

    @Override
    public Long countUserSubscribes(UUID userId) {
        return repository.countBySubscriberUuid(userId);
    }

    @Override
    public PageDto<UserResponseDto> getAllUserFollowers(UUID userId, Pageable page) {
        Page<UserEntity> followers = repository.findAllByFollowedUuid(userId, page).map(FollowEntity::getSubscriber);
        return UserMapper.toPageDto(followers);
    }

    @Override
    public Long countUserFollowers(UUID userId) {
        return repository.countByFollowedUuid(userId);
    }

    @Override
    public boolean isFollowing(UUID subscriberId, UUID followedId) {
        return repository.existsBySubscriber_UuidAndFollowed_Uuid(subscriberId, followedId);
    }
}
