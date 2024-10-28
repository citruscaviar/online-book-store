package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.CategoryRequestDto;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id,
                               CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
