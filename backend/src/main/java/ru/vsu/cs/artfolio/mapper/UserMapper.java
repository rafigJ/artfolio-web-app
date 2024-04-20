package ru.vsu.cs.artfolio.mapper;

import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;

public class UserMapper {

    public static UserResponseDto toDto(UserEntity user) {
        return UserResponseDto.builder()
                .uuid(user.getUuid())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(null)
                .build();
    }

    public static FullUserResponseDto toFullDto(UserEntity user) {
        return FullUserResponseDto.builder()
                .uuid(user.getUuid())
                .fullName(user.getFullName())
                .description(user.getAdditionalInfo())
                .country(user.getCountry())
                .city(user.getCity())
                .username(user.getUsername())
                .email(user.getEmail())
                .likeCount(null)
                .subscribersCount(null)
                .likeCount(null)
                .build();
    }
}
