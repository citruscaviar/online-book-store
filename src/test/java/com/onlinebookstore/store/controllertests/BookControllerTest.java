package com.onlinebookstore.store.controllertests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
@Sql(scripts = {
        "classpath:database/test/books/add-books-to-table.sql",
        "classpath:database/test/categories/add-categories-to-table.sql",
        "classpath:database/test/books/add-books-and-categories-into-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/test/books/delete-books-categories.sql",
        "classpath:database/test/books/remove-books-from-table-books.sql",
        "classpath:database/test/categories/delete-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Given valid book ID and update request, should return updated book")
    void updateByValidId_ShouldReturnUpdatedBook_Ok() throws Exception {
        long bookId = 12L;
        BookRequestDto updateRequestDto = createUpdateRequestDto();
        BookResponseDto expectedDto = createExpectedBookDto(bookId, updateRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(
                        put("/books/" + bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                BookResponseDto.class);
        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Given valid create request, should create a new book")
    void createBook_ValidRequest_ShouldCreateBook_Ok() throws Exception {
        BookRequestDto createBookRequestDto = createBookRequestDto();
        BookResponseDto expectedDto = createExpectedBookDtoWithoutId(createBookRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actualDto = objectMapper.readValue(result.getResponse()
                        .getContentAsString(),
                BookResponseDto.class);
        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
        Assertions.assertNotNull(actualDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Find all books in the catalog, should return all books")
    void findAllBooks_BooksExistInCatalog_ShouldReturnAllBooks_Ok() throws Exception {
        List<BookResponseDto> expected = createBooksList();

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsByteArray(),
                BookResponseDto[].class);

        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Find book by ID, should return the valid book")
    void findById_ValidId_ShouldReturnBook_Ok() throws Exception {
        long bookId = 12L;
        BookResponseDto expected = createBookDto(bookId);

        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsByteArray(),
                BookResponseDto.class);
        Assertions.assertEquals(expected, actual);
    }

    private BookRequestDto createUpdateRequestDto() {
        return new BookRequestDto()
                .setTitle("Updated Book Title")
                .setAuthor("Updated Author")
                .setDescription("Updated description for the book")
                .setIsbn("9783161484103")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCoverImage("http://example.com/updated-cover.jpg")
                .setCategoryIds(Set.of(13L));
    }

    private BookRequestDto createBookRequestDto() {
        return new BookRequestDto()
                .setTitle("Test Book 10")
                .setAuthor("Test Author 10")
                .setDescription("Valid Description with less than 255 characters")
                .setIsbn("9783161484110")
                .setPrice(BigDecimal.valueOf(39.99))
                .setCoverImage("http://example.com/cover10.jpg")
                .setCategoryIds(Set.of(12L, 13L));
    }

    private BookResponseDto createExpectedBookDto(long bookId, BookRequestDto requestDto) {
        return new BookResponseDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    private BookResponseDto createExpectedBookDtoWithoutId(BookRequestDto requestDto) {
        return new BookResponseDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    private List<BookResponseDto> createBooksList() {
        List<BookResponseDto> books = new ArrayList<>();
        books.add(new BookResponseDto()
                .setId(12L)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Description for Test Book 1")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoryIds(Set.of(12L)));
        books.add(new BookResponseDto()
                .setId(13L)
                .setTitle("Test Book 2")
                .setAuthor("Test Author 2")
                .setDescription("Description for Test Book 2")
                .setIsbn("9783161484101")
                .setPrice(BigDecimal.valueOf(29.99))
                .setCoverImage("http://example.com/cover2.jpg")
                .setCategoryIds(Set.of(12L)));
        return books;
    }

    private BookResponseDto createBookDto(long id) {
        return new BookResponseDto()
                .setId(id)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setDescription("Description for Test Book 1")
                .setIsbn("9783161484100")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("http://example.com/cover1.jpg")
                .setCategoryIds(Set.of(12L));
    }
}
