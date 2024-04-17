package ru.vsu.cs.artfolio.mapper;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class PostMapper {
    public static FullPostResponseDto toFullDto(PostEntity postEntity, UserResponseDto owner) {
        return FullPostResponseDto.builder()
                .id(postEntity.getId())
                .mediaIds(postEntity.getMedias().stream().sorted(Comparator.comparingInt(MediaFileEntity::getPosition)).map(MediaFileEntity::getId).toList())
                .name(postEntity.getName())
                .owner(owner)
                .description(postEntity.getDescription())
                .build();
    }

    public static PostEntity toEntity(PostRequestDto post, UserEntity owner, List<MultipartFile> files) throws IOException {
        return PostEntity.builder()
                .name(post.getName())
                .description(post.getDescription())
                .createTime(LocalDateTime.now())
                .owner(owner)
                .medias(MediaMapper.toEntityList(files))
                .build();
    }

    public static PostResponseDto toDto(PostEntity postEntity) {
        throw new UnsupportedOperationException();
    }

    public static Page<PostResponseDto> toPageDto(Page<PostEntity> postEntityPage) {
        throw new UnsupportedOperationException();
    }
}
