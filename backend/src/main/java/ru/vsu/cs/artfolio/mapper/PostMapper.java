package ru.vsu.cs.artfolio.mapper;

import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;

import java.util.List;

public class PostMapper {
    public static FullPostResponseDto toDto(PostEntity postEntity, UserResponseDto owner, List<Long> mediaIds) {
        return FullPostResponseDto.builder()
                .id(postEntity.getId())
                .mediaIds(mediaIds)
                .name(postEntity.getName())
                .owner(owner)
                .description(postEntity.getDescription())
                .build();
    }
}
