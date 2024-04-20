package ru.vsu.cs.artfolio.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class PostResponseDto {
    private Long id;
    private String name;
    private Long previewMediaId;
    private Long likeCount;
    private UserResponseDto owner;
}
