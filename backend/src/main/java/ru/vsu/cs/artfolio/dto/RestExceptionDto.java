package ru.vsu.cs.artfolio.dto;

import jakarta.validation.constraints.NotBlank;

public record RestExceptionDto(
        @NotBlank
        String message) {}
