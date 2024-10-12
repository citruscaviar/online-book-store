package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    BookResponseDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);
}
