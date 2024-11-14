package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.BookMapper;
import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.model.Category;
import com.onlinebookstore.store.repository.BookRepository;
import com.onlinebookstore.store.repository.CategoryRepository;
import com.onlinebookstore.store.service.BookService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAllWithCategories(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategory(Long id) {
        return bookMapper.toDtoWithoutCategories(bookRepository.findAllByCategoryId(id));
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find a book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookResponseDto update(Long id, BookRequestDto requestDto) {
        Set<Category> categories = categoryRepository
                    .findByIdIn(requestDto.getCategoryIds());
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find a book with id: " + id));
        book.setCategories(categories);
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
