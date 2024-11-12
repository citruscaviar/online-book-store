package com.onlinebookstore.store.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.store.dto.CategoryRequestDto;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.CategoryMapper;
import com.onlinebookstore.store.model.Category;
import com.onlinebookstore.store.repository.CategoryRepository;
import com.onlinebookstore.store.service.impl.CategoryServiceImpl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Finds category by id")
    void findsCategory_ById_ShouldReturnCategory_Ok() {
        Long id = 1L;
        Category category = createTestCategory();
        category.setId(id);
        CategoryResponseDto categoryResponseDto = createTestCategoryResponseDto();

        when(categoryRepository.findById(id)).thenReturn(java.util.Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto expectedCategoryResponseDto = categoryService.getById(id);

        assertEquals(expectedCategoryResponseDto, categoryResponseDto);
    }

    @Test
    @DisplayName("Finds category by id - should throw exception when category not found")
    void findsCategory_ById_ShouldThrowException_NotOk() {
        when(categoryRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(anyLong()));
    }

    @Test
    @DisplayName("Finds all categories")
    void findsAllCategories_ShouldReturnListDto_Ok() {
        List<Category> categoryList = createCategoryList();
        List<CategoryResponseDto> categoryResponseDtoList = createCategoryResponseDtoList();
        Pageable pageable = PageRequest.of(0, 10);

        // Create a Page<Category> mock
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        for (int i = 0; i < categoryList.size(); i++) {
            when(categoryMapper.toDto(categoryList.get(i)))
                    .thenReturn(categoryResponseDtoList.get(i));
        }

        List<CategoryResponseDto> expected = categoryService.findAll(pageable);

        assertNotNull(expected);
        assertEquals(expected.size(), categoryResponseDtoList.size());
    }

    @Test
    @DisplayName("Should save a category")
    void savesCategory_ShouldReturnResponseDto_Ok() {
        Category category = createTestCategory();
        CategoryResponseDto categoryResponseDto = createTestCategoryResponseDto();
        CategoryRequestDto categoryRequestDto = createTestCategoryRequestDto();

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto expected = categoryService.save(categoryRequestDto);

        assertEquals(expected, categoryResponseDto);
    }

    @Test
    @DisplayName("Should update a category and return the updated DTO")
    void update_ShouldReturnUpdatedCategoryResponseDto_Ok() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createTestCategoryRequestDto();
        Category category = createTestCategory();
        Category updatedCategory = createTestUpdatedCategory();
        CategoryResponseDto expectedDto = createTestCategoryResponseDto();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedDto);

        CategoryResponseDto result = categoryService.update(categoryId, requestDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when category not found during update")
    void update_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createTestCategoryRequestDto();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, requestDto));

        assertEquals("Couldn't find a category by id: " + categoryId, exception.getMessage());
    }

    @Test
    @DisplayName("Deletes a category by ID")
    void deleteById_ShouldInvokeRepositoryDelete_Ok() {
        Long id = 1L;

        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setName("Fiction");
        return category;
    }

    private CategoryResponseDto createTestCategoryResponseDto() {
        return new CategoryResponseDto().setName("Fiction");
    }

    private CategoryRequestDto createTestCategoryRequestDto() {
        return new CategoryRequestDto().setName("Fiction");
    }

    private List<Category> createCategoryList() {
        Category category1 = new Category();
        category1.setName("Fiction");

        Category category2 = new Category();
        category2.setName("Non-Fiction");

        return List.of(category1, category2);
    }

    private List<CategoryResponseDto> createCategoryResponseDtoList() {
        CategoryResponseDto categoryResponseDto1 = new CategoryResponseDto().setName("Fiction");
        CategoryResponseDto categoryResponseDto2 = new CategoryResponseDto().setName("Non-Fiction");
        return List.of(categoryResponseDto1, categoryResponseDto2);
    }

    private Category createTestUpdatedCategory() {
        Category updatedCategory = createTestCategory();
        updatedCategory.setName("Updated Fiction");
        return updatedCategory;
    }
}
