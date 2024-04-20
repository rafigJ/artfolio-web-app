package ru.vsu.cs.artfolio.dto.post;

import lombok.Builder;

@Builder
public record PostRequestDto(
        String name,
        String description
) {
}
