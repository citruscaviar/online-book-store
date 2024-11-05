package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.order.OrderRequestDto;
import com.onlinebookstore.store.dto.order.OrderResponseDto;
import com.onlinebookstore.store.dto.order.OrderUpdateDto;
import com.onlinebookstore.store.dto.order.item.OrderItemResponseDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> findAllOrders(Long userId, Pageable pageable);

    OrderResponseDto create(Long userId, OrderRequestDto orderRequestDto);

    OrderResponseDto setUpdateStatus(Long userId, OrderUpdateDto orderUpdateDto);

    OrderItemResponseDto findOrderItemById(Long orderId, Long orderItemId, Long id);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId, Long userId);
}
