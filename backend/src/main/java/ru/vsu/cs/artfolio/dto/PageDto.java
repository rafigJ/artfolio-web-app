package ru.vsu.cs.artfolio.dto;

import java.util.List;

public record PageDto<T>(List<T> content, Long totalElements, Integer totalPages) {
}
