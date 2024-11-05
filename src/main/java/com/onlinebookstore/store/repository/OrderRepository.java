package com.onlinebookstore.store.repository;

import com.onlinebookstore.store.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    List<Order> findOrdersByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);
}

