package com.onlinebookstore.store.controller;

import com.onlinebookstore.store.dto.cart.CartItemRequestDto;
import com.onlinebookstore.store.dto.cart.CartItemUpdateDto;
import com.onlinebookstore.store.dto.cart.ShoppingCartDto;
import com.onlinebookstore.store.model.User;
import com.onlinebookstore.store.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "Shopping Cart", description = "Endpoints for managing shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve user's shopping cart",
            description = "Retrieves the user's shopping cart")
    public ShoppingCartDto getShoppingCart(
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to the shopping cart",
            description = "Adds a book to the user's shopping cart")
    public ShoppingCartDto addItemToCart(
            Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItemToShoppingCart(user.getId(),
                cartItemRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Updates the quantity of a book in the user's shopping cart")
    public ShoppingCartDto updateCartItem(
            Authentication authentication,
            @PathVariable @Positive Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(user.getId(),
                cartItemId, cartItemRequestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Removes a book from the user's shopping cart")
    public void deleteCartItem(
            Authentication authentication,
            @PathVariable @Positive Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.removeCartItem(user.getId(), cartItemId);
    }
}
