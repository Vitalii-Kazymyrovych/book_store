package com.project.bookstore.dto;

import com.project.bookstore.validation.Isbn;
import com.project.bookstore.validation.Url;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateBookRequestDto(
        @NotNull String title,
        @NotNull String author,
        @Isbn String isbn,
        @Min(0) @NotNull BigDecimal price,
        @NotNull String description,
        @Url String coverImage) {
}
