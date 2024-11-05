package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.order.OrderRequestDto;
import com.onlinebookstore.store.dto.order.OrderResponseDto;
import com.onlinebookstore.store.dto.order.OrderUpdateDto;
import com.onlinebookstore.store.dto.order.item.OrderItemResponseDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.OrderItemMapper;
import com.onlinebookstore.store.mapper.OrderMapper;
import com.onlinebookstore.store.mapper.ShoppingCartMapper;
import com.onlinebookstore.store.model.Order;
import com.onlinebookstore.store.model.OrderItem;
import com.onlinebookstore.store.model.ShoppingCart;
import com.onlinebookstore.store.repository.OrderItemRepository;
import com.onlinebookstore.store.repository.OrderRepository;
import com.onlinebookstore.store.repository.ShoppingCartRepository;
import com.onlinebookstore.store.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable) {
        return orderMapper.toDtoList(orderRepository
                .findOrdersByUserId(userId));
    }

    @Override
    @Transactional
    public OrderResponseDto create(Long userId, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        Order userOrder = orderRepository.save(
                initializeNewOrder(shoppingCart, orderRequestDto));
        orderItemRepository.saveAll(userOrder.getOrderItems()
                .stream()
                .peek(orderItem -> orderItem.setOrder(userOrder))
                .collect(Collectors.toSet()));
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(userOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto setUpdateStatus(Long orderId, OrderUpdateDto orderUpdateDto) {
        Order order = getOrderById(orderId);
        Order.Status status = Order.Status.valueOf(orderUpdateDto
                .status()
                .name()
                .toUpperCase());
        order.setStatus(status);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderItemResponseDto findOrderItemById(Long orderId, Long orderItemId, Long userId) {
        return orderItemMapper.toDto(
                orderItemRepository.findOrderItemByOrderIdAndIdAndUserId(
                            orderId, orderItemId, userId).orElseThrow(
                                () -> new EntityNotFoundException("Cannot find item by id: "
                                + orderItemId))
        );
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId, Long userId) {
        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    private Order initializeNewOrder(ShoppingCart shoppingCart, OrderRequestDto requestDto) {
        Order newOrder = orderMapper.toOrder(shoppingCart);
        newOrder.setShippingAddress(requestDto.shippingAddress());
        newOrder.setTotal(countTotal(newOrder));
        return newOrder;
    }

    private BigDecimal countTotal(Order newOrder) {
        return newOrder.getOrderItems()
                .stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ShoppingCart getShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: " + userId)
        );
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find order by id: " + orderId));
    }
}

