package com.project.bookstore.dto.book;

import com.project.bookstore.validation.Isbn;
import com.project.bookstore.validation.Url;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateBookRequestDto(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String author,
        @Isbn String isbn,
        @NotNull @Min(0) BigDecimal price,
        @NotNull @NotBlank String description,
        @Url String coverImage,
        @NotNull List<Long> categoryIds) {
}
