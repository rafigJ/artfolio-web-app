package ru.vsu.cs.artfolio.dto.post;

import lombok.*;
import ru.vsu.cs.artfolio.dto.UserResponseDto;

import java.util.List;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class FullPostResponseDto {
    public UserResponseDto owner;
    public Long id;
    public String name;
    public String description;
    public List<Long> mediaIds;
}
