package ru.vsu.cs.artfolio.dto.report;

import lombok.NonNull;

public record ReportReviewRequestDto(
        @NonNull
        Boolean reviewed
) { }
