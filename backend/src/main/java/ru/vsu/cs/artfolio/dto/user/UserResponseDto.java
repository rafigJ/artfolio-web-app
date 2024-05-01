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
public final class UserResponseDto {
    public UUID uuid;
    public String fullName;
    public String email;
    public String username;
}
