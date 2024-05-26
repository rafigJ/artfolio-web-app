package ru.vsu.cs.artfolio.dto.report;

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
public final class PostReportResponseDto {

    @NonNull
    private Long id;

    @NonNull
    private Long postId;

    @NonNull
    private Boolean reviewed;

    @NonNull
    private LocalDateTime createTime;

    @NonNull
    private UserResponseDto targetUser;

    @NonNull
    private UserResponseDto sender;
}
