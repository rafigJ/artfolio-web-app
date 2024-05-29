package ru.vsu.cs.artfolio.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportRequestDto(
        @NotBlank
        @Size(max = 300)
        String reason
) {}
