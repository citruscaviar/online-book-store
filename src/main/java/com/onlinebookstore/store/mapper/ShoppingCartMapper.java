package com.onlinebookstore.store.mapper;

import com.onlinebookstore.store.config.MapperConfig;
import com.onlinebookstore.store.dto.cart.ShoppingCartDto;
import com.onlinebookstore.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "userId", target = "user.id")
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}
