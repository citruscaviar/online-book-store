package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.cart.CartItemRequestDto;
import com.onlinebookstore.store.dto.cart.CartItemUpdateDto;
import com.onlinebookstore.store.dto.cart.ShoppingCartDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.ShoppingCartMapper;
import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.model.CartItem;
import com.onlinebookstore.store.model.ShoppingCart;
import com.onlinebookstore.store.model.User;
import com.onlinebookstore.store.repository.BookRepository;
import com.onlinebookstore.store.repository.CartItemRepository;
import com.onlinebookstore.store.repository.ShoppingCartRepository;
import com.onlinebookstore.store.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper
                .toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItemToShoppingCart(Long userId,
                                                     CartItemRequestDto itemDto) {
        Book book = bookRepository.findById(itemDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book"));

        ShoppingCart cart = getShoppingCartByUserId(userId);
        cart.getCartItems().stream().filter(cartItem -> cartItem.getBook().getId()
                        .equals(itemDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity()
                                + itemDto.getQuantity()),
                        () -> addCartItemToShoppingCart(itemDto, book, cart));

        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    private void addCartItemToShoppingCart(CartItemRequestDto itemDto,
                                           Book book, ShoppingCart cart) {
        CartItem newCartItem = new CartItem();
        newCartItem.setBook(book);
        newCartItem.setQuantity(itemDto.getQuantity());
        newCartItem.setShoppingCart(cart);
        cart.getCartItems().add(newCartItem);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItem(Long userId,
                                          Long cartItemId,
                                          CartItemUpdateDto request) {
        cartItemRepository.updateCartItemQuantityById(
                request.getQuantity(),
                cartItemId);
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCart.getId());
        shoppingCart.removeItemFromCart(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    public void createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository
                .findByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user by id" + userId
                        )
                );
    }
}
