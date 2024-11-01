package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.cart.CartItemRequestDto;
import com.onlinebookstore.store.dto.cart.CartItemUpdateDto;
import com.onlinebookstore.store.dto.cart.ShoppingCartDto;
import com.onlinebookstore.store.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long id);

    ShoppingCartDto addCartItemToShoppingCart(Long userId,
                                              CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateCartItem(Long userId,
                                   Long cartItemId,
                                   CartItemUpdateDto request);

    ShoppingCartDto removeCartItem(Long userId, Long cartItemId);

    void createNewShoppingCart(User user);
}
