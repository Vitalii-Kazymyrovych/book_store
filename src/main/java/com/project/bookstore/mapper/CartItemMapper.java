package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.cart.item.CartItemDto;
import com.project.bookstore.dto.cart.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.cart.item.CreateCartItemRequestDto;
import com.project.bookstore.model.CartItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    CartItemWithoutBookTitleDto toCartItemWithoutBookTitleDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Named("toCartItemDtoList")
    default List<CartItemDto> toCartItemDtoList(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .toList();
    }
}
