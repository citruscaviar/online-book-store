package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBooksByCategory(Long id);

    BookResponseDto findById(Long id);

    BookResponseDto update(Long id, BookRequestDto requestDto);

    void delete(Long id);
}
