package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.model.Order;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemList", source = "orderItems", qualifiedByName = "toOrderItemList")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "oderDate", source = "orderDate", qualifiedByName = "dateToString")
    OrderDto toDto(Order order);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "oderDate", source = "orderDate", qualifiedByName = "dateToString")
    OrderWithoutItemsDto toOrderWithoutItemsDto(Order order);

    @Named("statusToString")
    default String statusToString(Order.Status status) {
        return status.name();
    }

    @Named("dateToString")
    default String dateToString(LocalDateTime orderDate) {
        return orderDate.toString();
    }
}
