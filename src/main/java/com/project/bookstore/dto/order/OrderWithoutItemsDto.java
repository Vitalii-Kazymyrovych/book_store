package com.project.bookstore.dto.order;

import java.math.BigDecimal;

public record OrderWithoutItemsDto(
        Long id,
        Long userId,
        String oderDate,
        BigDecimal total,
        String status) {
}
