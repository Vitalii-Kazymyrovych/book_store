package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.shopping.item.CartItemDto;
import com.project.bookstore.dto.shopping.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.shopping.item.CreateCartItemRequestDto;
import com.project.bookstore.model.CartItem;
import com.project.bookstore.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        config = MapperConfig.class,
        uses = {BookMapper.class, CartItemMapper.class},
        imports = {BigDecimal.class})
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    CartItemWithoutBookTitleDto toCartItemWithoutBookTitleDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(
            target = "price",
            expression = "java(cartItem.getBook().getPrice().multiply("
                    + "BigDecimal.valueOf(cartItem.getQuantity())))")
    OrderItem toOrderItem(CartItem cartItem);

    @Named("toOrderItems")
    default Set<OrderItem> toOrderItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
    }

    @Named("toCartItemDtoList")
    default List<CartItemDto> toCartItemDtoList(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .toList();
    }
}
