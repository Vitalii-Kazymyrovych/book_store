package com.project.bookstore.dto.order;

import com.project.bookstore.dto.order.item.OrderItemDto;
import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        Long id,
        Long userId,
        List<OrderItemDto> orderItemList,
        String oderDate,
        BigDecimal total,
        String status) {
}
