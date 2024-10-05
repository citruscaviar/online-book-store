package com.onlinebookstore.store.repository;

import com.onlinebookstore.store.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
