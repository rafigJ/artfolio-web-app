package ru.vsu.cs.artfolio.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDto(String name, String email, String role, String token) {}
