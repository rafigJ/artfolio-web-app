package ru.vsu.cs.artfolio.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class PostResponseDto {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long previewMediaId;

    private Long likeCount;

    @NonNull
    private UserResponseDto owner;
}
