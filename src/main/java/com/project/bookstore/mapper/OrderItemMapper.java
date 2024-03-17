package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.order.item.OrderItemDto;
import com.project.bookstore.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Named("toOrderItemList")
    default List<OrderItemDto> toOrderItemList(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .toList();
    }
}
