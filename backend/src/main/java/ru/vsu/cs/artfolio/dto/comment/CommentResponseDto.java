package ru.vsu.cs.artfolio.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class CommentResponseDto {

    @NonNull
    private Long id;

    @NonNull
    private UserResponseDto owner;

    @NonNull
    private String comment;

    @NonNull
    private LocalDateTime createTime;

}
