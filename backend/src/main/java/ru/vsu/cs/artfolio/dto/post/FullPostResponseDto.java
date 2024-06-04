package ru.vsu.cs.artfolio.dto.post;

import lombok.*;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class FullPostResponseDto {
    public Long id;
    public String name;
    public String description;
    public Long likeCount;

    @Deprecated
    public Long previewMedia;

    public List<Long> mediaIds;
    public UserResponseDto owner;
}
