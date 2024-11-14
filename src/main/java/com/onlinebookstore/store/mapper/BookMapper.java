package com.onlinebookstore.store.mapper;

import com.onlinebookstore.store.config.MapperConfig;
import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookResponseDto toDto(Book book);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toModel(BookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(BookRequestDto requestDto, @MappingTarget Book book);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategories(List<Book> books);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookResponseDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
