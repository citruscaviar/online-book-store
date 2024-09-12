package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.BookDto;
import com.onlinebookstore.store.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
