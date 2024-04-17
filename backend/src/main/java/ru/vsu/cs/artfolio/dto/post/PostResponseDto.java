package ru.vsu.cs.artfolio.dto.post;

import ru.vsu.cs.artfolio.dto.UserResponseDto;

public record PostResponseDto(
        Long id,
        String name,
        Long previewMediaId,
        Long likeCount,
        UserResponseDto owner
) {
}
