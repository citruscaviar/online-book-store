package com.onlinebookstore.store.mapper;

import com.onlinebookstore.store.config.MapperConfig;
import com.onlinebookstore.store.dto.BookDto;
import com.onlinebookstore.store.dto.CreateBookRequestDto;
import com.onlinebookstore.store.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id",ignore = true)
    Book toModel(CreateBookRequestDto requestDto);
}