package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

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

    public static PostEntity toEntity(PostRequestDto post, UserEntity owner) {
        return PostEntity.builder()
                .name(post.getName())
                .description(post.getDescription())
                .createTime(LocalDateTime.now())
                .owner(owner)
                .build();
    }

    public static PostResponseDto toDto(PostEntity postEntity, Long previewMediaId) {
        return PostResponseDto.builder()
                .id(postEntity.getId())
                .previewMediaId(previewMediaId)
                .owner(UserMapper.toDto(postEntity.getOwner()))
                .likeCount(null)
                .build();
    }


    public static PageDto<PostResponseDto> toPageDto(Page<PostEntity> postEntityPage, List<Long> previewMediaIds) {
        if (previewMediaIds.size() != postEntityPage.getContent().size()) {
            throw new IllegalArgumentException("previewMediaIds size must be equal postEntityPage size");
        }
        List<PostEntity> postEntityList = postEntityPage.getContent();
        List<PostResponseDto> mappedPosts = new ArrayList<>();
        for (int i = 0; i < postEntityList.size(); i++) {
            mappedPosts.add(toDto(postEntityList.get(i), previewMediaIds.get(i)));
        }
        return new PageDto<>(mappedPosts, postEntityPage.getTotalElements(), postEntityPage.getTotalPages());
    }
}
