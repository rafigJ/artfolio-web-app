package ru.vsu.cs.artfolio.dto.post;

import ru.vsu.cs.artfolio.dto.UserResponseDto;

public record PostResponseDto(
        UserResponseDto owner,
        String name,
        String description,
        Long previewMediaId
) {
}
