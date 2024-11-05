package com.onlinebookstore.store.dto.order;

import com.onlinebookstore.store.model.Order;

public record OrderUpdateDto(
        Order.Status status
) {
}
