package com.onlinebookstore.store.service;

import com.onlinebookstore.store.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
