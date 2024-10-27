package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.CategoryRequestDto;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id,
                               CategoryRequestDto categoryDto);

    void deleteById(Long id);

}
