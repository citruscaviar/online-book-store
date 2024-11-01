package com.onlinebookstore.store.repository;

import com.onlinebookstore.store.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"user"})
    Optional<ShoppingCart> findByUserId(Long userId);
}
