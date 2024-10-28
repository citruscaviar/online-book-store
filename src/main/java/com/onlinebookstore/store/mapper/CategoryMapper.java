package com.onlinebookstore.store.mapper;

import com.onlinebookstore.store.config.MapperConfig;
import com.onlinebookstore.store.dto.CategoryRequestDto;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import com.onlinebookstore.store.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void updateCategory(CategoryRequestDto requestDto, @MappingTarget Category category);
}
