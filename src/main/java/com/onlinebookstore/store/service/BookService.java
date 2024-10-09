package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.BookDto;
import com.onlinebookstore.store.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);
}
