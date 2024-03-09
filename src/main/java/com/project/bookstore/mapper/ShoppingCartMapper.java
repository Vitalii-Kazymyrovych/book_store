package com.project.bookstore.mapper;

import com.project.bookstore.config.MapperConfig;
import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems", source = "cartItems", qualifiedByName = "toCartItemDtoList")
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
