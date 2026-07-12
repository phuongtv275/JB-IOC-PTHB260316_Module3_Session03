package com.example.coursemanagementsystem.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        int totalItems,
        int totalPages,
        boolean isLast
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                (int) Math.min(Integer.MAX_VALUE, page.getTotalElements()),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
