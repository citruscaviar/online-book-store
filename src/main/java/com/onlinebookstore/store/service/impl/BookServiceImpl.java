package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.dto.CreateBookRequestDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.BookMapper;
import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.repository.BookRepository;
import com.onlinebookstore.store.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find a book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find a book with id: " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
