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
    public void deleteSubscribe(UUID subscriberUuid, UUID followedUuid) {
        Optional<FollowEntity> followEntity = repository.findBySubscriber_UuidAndFollowed_Uuid(subscriberUuid, followedUuid);
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
    public PageDto<UserResponseDto> getAllUserFollowers(UUID userId, Pageable page) {
        Page<UserEntity> followers = repository.findAllByFollowedUuid(userId, page).map(FollowEntity::getSubscriber);
        return UserMapper.toPageDto(followers);
    }
}
