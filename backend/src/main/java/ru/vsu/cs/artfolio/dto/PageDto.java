package ru.vsu.cs.artfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> content;

    private Long totalElements;

    private Integer totalPages;
}
