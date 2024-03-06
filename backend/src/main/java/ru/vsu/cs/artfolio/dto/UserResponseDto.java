package ru.vsu.cs.artfolio.dto;

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
    public String name;
    public String email;
}
