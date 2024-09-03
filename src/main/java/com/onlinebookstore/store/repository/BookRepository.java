package com.onlinebookstore.store.repository;

import com.onlinebookstore.store.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
