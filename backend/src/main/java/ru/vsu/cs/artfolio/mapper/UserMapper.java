package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;
import ru.vsu.cs.artfolio.mapper.wrappers.UserAdditionalInfo;

import java.time.LocalDateTime;
import java.util.List;

public class UserMapper {

    public static UserResponseDto toDto(UserEntity user) {
        return UserResponseDto.builder()
                .uuid(user.getUuid())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .city(user.getCity())
                .country(user.getCountry())
                .build();
    }

    public static FullUserResponseDto toFullDto(UserEntity user, UserAdditionalInfo additionalInfo) {
        return FullUserResponseDto.builder()
                .uuid(user.getUuid())
                .fullName(user.getFullName())
                .description(user.getAdditionalInfo())
                .country(user.getCountry())
                .city(user.getCity())
                .username(user.getUsername())
                .email(user.getEmail())
                .postCount(additionalInfo.postCount())
                .subscribersCount(additionalInfo.subscribersCount())
                .followingCount(additionalInfo.followingCount())
                .likeCount(additionalInfo.likeCount())
                .isFollowed(additionalInfo.isFollowed())
                .build();
    }

    public static UserEntity updateEntity(UserEntity oldEntity, UserUpdateRequestDto updatedUser, MinioResult avatarData) {
        oldEntity.setFullName(updatedUser.fullName());
        oldEntity.setAdditionalInfo(updatedUser.description());
        oldEntity.setCountry(updatedUser.country());
        oldEntity.setCity(updatedUser.city());
        oldEntity.setUsername(updatedUser.username());
        oldEntity.setAvatarName(avatarData.name());
        oldEntity.setAvatarType(avatarData.contentType());
        oldEntity.setUpdateTime(LocalDateTime.now());
        return oldEntity;
    }

    public static PageDto<UserResponseDto> toPageDto(Page<UserEntity> userEntityPage) {
        List<UserResponseDto> userResponseDtoList = userEntityPage.getContent().stream().map(UserMapper::toDto).toList();
        return new PageDto<>(userResponseDtoList, userEntityPage.getTotalElements(), userEntityPage.getTotalPages());
    }
}
