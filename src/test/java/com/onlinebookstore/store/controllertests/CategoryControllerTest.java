package com.onlinebookstore.store.controllertests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.CategoryResponseDto;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/test/categories/add-categories-to-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/test/books/delete-books-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/test/categories/delete-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create Category")
    void createCategory_ValidRequest_ShouldSaveCategory_Ok() throws Exception {
        CategoryResponseDto createCategoryDto = new CategoryResponseDto()
                .setName("New Category")
                .setDescription("Category description");

        String jsonRequest = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class);
        Assertions.assertEquals(createCategoryDto
                        .getName(),
                actualDto.getName());
        Assertions.assertEquals(createCategoryDto
                        .getDescription(),
                actualDto.getDescription());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all categories")
    void getAllCategories_ShouldReturnCategoriesList_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryResponseDto[].class);
        Assertions.assertTrue(actual.length > 0);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update Category")
    void updateCategory_ValidId_ShouldReturnUpdatedCategory_Ok() throws Exception {
        long categoryId = 12L;
        CategoryResponseDto updateCategoryDto = new CategoryResponseDto()
                .setName("Updated Category")
                .setDescription("Updated description");

        String jsonRequest = objectMapper.writeValueAsString(updateCategoryDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/" + categoryId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class);
        Assertions.assertEquals(updateCategoryDto.getName(), actualDto.getName());
        Assertions.assertEquals(updateCategoryDto.getDescription(), actualDto.getDescription());
    }

    private CategoryResponseDto createTestCategoryResponseDto() {
        return new CategoryResponseDto()
                .setId(1L)
                .setName("Test Category")
                .setDescription("Test description");
    }

    private List<BookDtoWithoutCategoryIds> createTestBookDtoList() {
        BookDtoWithoutCategoryIds bookDto1 = new BookDtoWithoutCategoryIds()
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setPrice(new BigDecimal("10.99"))
                .setIsbn("9783161484100")
                .setDescription("Description 1")
                .setCoverImage("http://example.com/cover1.jpg");

        BookDtoWithoutCategoryIds bookDto2 = new BookDtoWithoutCategoryIds()
                .setTitle("Book 2")
                .setAuthor("Author 2")
                .setPrice(new BigDecimal("12.99"))
                .setIsbn("9783161484101")
                .setDescription("Description 2")
                .setCoverImage("http://example.com/cover2.jpg");

        return List.of(bookDto1, bookDto2);
    }
}
