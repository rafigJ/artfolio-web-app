package ru.vsu.cs.artfolio.dto;

import lombok.Builder;

import java.io.InputStream;

@Builder
public record MediaDto(InputStream fileStream, String contentType) {
}
