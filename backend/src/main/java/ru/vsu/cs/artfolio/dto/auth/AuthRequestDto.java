package ru.vsu.cs.artfolio.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(@NotBlank(message = "should be not blank") String email,
                             @NotBlank(message = "should be not empty") String password) {
}
