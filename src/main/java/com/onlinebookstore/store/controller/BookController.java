package com.onlinebookstore.store.controller;

import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.service.BookService;
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

@Tag(name = "Book management", description = "Managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Finds all books",
            description = "Implements sorting and pagination")
    public List<BookResponseDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Finds a book by id")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a new book",
            description = "Checks if a field you entered is valid")
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates an existing book",
            description = "Checks if a field you entered is valid")
    public BookResponseDto updateBook(@PathVariable Long id,
                                      @RequestBody @Valid BookRequestDto bookDto) {
        return bookService.update(id,bookDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a book",
            description = "Uses soft-delete")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
