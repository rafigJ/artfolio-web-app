package ru.vsu.cs.artfolio.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @Size(max = 300)
        @NotBlank
        String comment
) {
}
