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

    public static FullUserResponseDto toFullDto() {
        throw new UnsupportedOperationException();
    }
}
