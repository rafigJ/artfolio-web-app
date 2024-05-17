package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostMapper {
    public static FullPostResponseDto toFullDto(PostEntity postEntity, List<Long> mediaIds) {
        return FullPostResponseDto.builder()
                .id(postEntity.getId())
                .mediaIds(mediaIds)
                .name(postEntity.getName())
                .owner(UserMapper.toDto(postEntity.getOwner()))
                .description(postEntity.getDescription())
                .build();
    }

    public static PostEntity toEntity(PostRequestDto post, UserEntity owner, MinioResult previewMedia) {
        return PostEntity.builder()
                .name(post.getName())
                .description(post.getDescription())
                .createTime(LocalDateTime.now())
                .owner(owner)
                .previewMediaName(previewMedia.name())
                .previewType(previewMedia.contentType())
                .build();
    }

    public static PostResponseDto toDto(PostEntity postEntity) {
        return PostResponseDto.builder()
                .id(postEntity.getId())
                .name(postEntity.getName())
                .owner(UserMapper.toDto(postEntity.getOwner()))
                .likeCount(null)
                .build();
    }


    public static PageDto<PostResponseDto> toPageDto(Page<PostEntity> postEntityPage) {
        List<PostResponseDto> postResponseDto = postEntityPage.getContent().stream().map(PostMapper::toDto).toList();
        return new PageDto<>(postResponseDto, postEntityPage.getTotalElements(), postEntityPage.getTotalPages());
    }
}
