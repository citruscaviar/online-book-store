package com.onlinebookstore.store.controller;

import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.CategoryRequestDto;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import com.onlinebookstore.store.service.BookService;
import com.onlinebookstore.store.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Managing Category")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @Operation(description = "Creates a category for the book")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping("/{id}/books")
    @Operation(description = "Finds books by category")
    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookService.getBooksByCategory(id);
    }

    @GetMapping
    @Operation(description = "Finds all categories")
    public List<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(description = "Finds a category by id")
    public CategoryResponseDto getCategoryById(Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "Updates a category by id")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto categoryDto
    ) {
        return categoryService.update(id, categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Deletes a category by id")
    public void deleteCategory(Long id) {
        categoryService.deleteById(id);
    }
}
