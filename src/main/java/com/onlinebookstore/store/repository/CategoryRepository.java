package com.onlinebookstore.store.repository;

import com.onlinebookstore.store.model.Category;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findByIdIn(Set<Long> categoryIds);
}
