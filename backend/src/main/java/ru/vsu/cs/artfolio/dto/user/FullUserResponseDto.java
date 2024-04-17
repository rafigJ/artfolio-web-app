package ru.vsu.cs.artfolio.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class FullUserResponseDto {
    public UUID uuid;
    public String fullName;
    public String description;
    public String country;
    public String city;
    public String username;
    public String email;
    public Long postCount;
    public Long subscribersCount;
    public Long likeCount;
}
