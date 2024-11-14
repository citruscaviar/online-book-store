package com.onlinebookstore.store.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.onlinebookstore.store.dto.BookDtoWithoutCategoryIds;
import com.onlinebookstore.store.dto.BookRequestDto;
import com.onlinebookstore.store.dto.BookResponseDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.mapper.BookMapper;
import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.model.Category;
import com.onlinebookstore.store.repository.BookRepository;
import com.onlinebookstore.store.repository.CategoryRepository;
import com.onlinebookstore.store.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Finds books by id")
    void findsBook_ById_ShouldReturnBook_Ok() {
        Long id = 1L;
        Book book = createTestBook();
        book.setId(id);
        BookResponseDto bookResponseDto = createTestBookResponseDto();

        when(bookRepository.findBookById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookResponseDto);

        BookResponseDto expectedBookResponseDto = bookService.findById(id);

        assertEquals(expectedBookResponseDto, bookResponseDto);
    }

    @Test
    @DisplayName("Finds books by id")
    void findsBook_ById_ShouldThrowException_NotOk() {
        when(bookRepository
                .findBookById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(anyLong()));
    }

    @Test
    @DisplayName("Finds all books")
    void findsAllBooks_ShouldReturnListDto_Ok() {
        List<Book> bookList = createBookList();
        List<BookResponseDto> bookResponseDtoList = createBookResponseDtoList();
        Pageable pageable = PageRequest.of(0,10);

        when(bookRepository.findAllWithCategories(pageable)).thenReturn(bookList);
        for (int i = 0; i < bookList.size(); i++) {
            when(bookMapper.toDto(bookList.get(i))).thenReturn(bookResponseDtoList.get(i));
        }

        List<BookResponseDto> expected = bookService.findAll(pageable);

        assertNotNull(expected);
        assertEquals(expected.size(), bookResponseDtoList.size());
    }

    @Test
    @DisplayName("Should save a book")
    void savesBook_ShouldReturnResponseDto_Ok() {
        Book book = createTestBook();
        BookResponseDto bookResponseDto = createTestBookResponseDto();
        BookRequestDto bookRequestDto = createTestBookRequestDto();

        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookResponseDto);

        BookResponseDto expected = bookService.save(bookRequestDto);

        assertEquals(expected, bookResponseDto);
    }

    @Test
    @DisplayName("Should return books without category IDs")
    void getsBooksByCategory_ShouldReturnDtosWithoutCategoryIds_Ok() {
        Long categoryId = 1L;

        List<Book> books = createBookList();
        List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIdsList
                = createBookDtoWithoutCategoryIdsList();

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(books)).thenReturn(bookDtoWithoutCategoryIdsList);

        List<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategory(categoryId);

        assertNotNull(result);
        assertEquals(bookDtoWithoutCategoryIdsList.size(), result.size());
        assertEquals(bookDtoWithoutCategoryIdsList, result);
    }

    @Test
    @DisplayName("Should update a book and return the updated DTO")
    void update_ShouldReturnUpdatedBookResponseDto_Ok() {
        Long bookId = 1L;
        BookRequestDto requestDto = createTestBookRequestDto();
        Book book = createTestBook();
        Book updatedBook = createTestUpdatedBook();
        Set<Category> categories = createTestCategorySet();
        BookResponseDto expectedDto = createTestBookResponseDto();

        when(categoryRepository.findByIdIn(requestDto.getCategoryIds())).thenReturn(categories);
        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(requestDto, book);
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedDto);

        BookResponseDto result = bookService.update(bookId, requestDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book not found")
    void update_ShouldThrowEntityNotFoundException_WhenBookNotFound() {
        Long bookId = 1L;
        BookRequestDto requestDto = createTestBookRequestDto();

        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(bookId, requestDto));

        assertEquals("Couldn't find a book with id: " + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("Deletes a book by ID")
    void deleteById_ShouldInvokeRepositoryDelete_Ok() {
        Long id = 1L;

        doNothing().when(bookRepository).deleteById(id);

        bookService.delete(id);

        verify(bookRepository, times(1)).deleteById(id);
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setTitle("Test title");
        book.setAuthor("Test author");
        book.setPrice(new BigDecimal("3.45"));
        book.setIsbn("9783161484100");
        book.setDescription("Test description");
        book.setCoverImage("http://example.com/cover.jpg");
        return book;
    }

    private BookResponseDto createTestBookResponseDto() {
        return new BookResponseDto()
                .setTitle("Test title")
                .setAuthor("Test author")
                .setPrice(new BigDecimal("3.45"))
                .setIsbn("9783161484100")
                .setDescription("Test description")
                .setCoverImage("http://example.com/cover.jpg");
    }

    private BookRequestDto createTestBookRequestDto() {
        return new BookRequestDto()
                .setTitle("Test title")
                .setAuthor("Test author")
                .setPrice(new BigDecimal("3.45"))
                .setIsbn("9783161484100")
                .setDescription("Test description")
                .setCoverImage("http://example.com/cover.jpg");
    }

    private List<Book> createBookList() {
        Book book1 = new Book();
        book1.setTitle("Test title 1");
        book1.setAuthor("Test author 1");
        book1.setPrice(new BigDecimal("3.45"));
        book1.setIsbn("9783161484100");
        book1.setDescription("Test description 1");
        book1.setCoverImage("http://example1.com/cover.jpg");

        Book book2 = new Book();
        book2.setTitle("Test title 2");
        book2.setAuthor("Test author 2");
        book2.setPrice(new BigDecimal("5.45"));
        book2.setIsbn("9783161484101");
        book2.setDescription("Test description 2");
        book2.setCoverImage("http://example2.com/cover.jpg");

        return List.of(book1, book2);
    }

    private List<BookResponseDto> createBookResponseDtoList() {
        BookResponseDto bookResponseDto1 = new BookResponseDto()
                .setTitle("Test title 1")
                .setAuthor("Test author 1")
                .setPrice(new BigDecimal("3.45"))
                .setIsbn("9783161484100")
                .setDescription("Test description 1")
                .setCoverImage("http://example1.com/cover.jpg");

        BookResponseDto bookResponseDto2 = new BookResponseDto()
                .setTitle("Test title 2")
                .setAuthor("Test author 2")
                .setPrice(new BigDecimal("5.45"))
                .setIsbn("9783161484101")
                .setDescription("Test description 2")
                .setCoverImage("http://example2.com/cover.jpg");
        return List.of(bookResponseDto1, bookResponseDto2);
    }

    private List<BookDtoWithoutCategoryIds> createBookDtoWithoutCategoryIdsList() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds1 = new BookDtoWithoutCategoryIds()
                .setTitle("Test title 1")
                .setAuthor("Test author 1")
                .setPrice(new BigDecimal("3.45"))
                .setIsbn("9783161484100")
                .setDescription("Test description 1")
                .setCoverImage("http://example1.com/cover.jpg");

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds2 = new BookDtoWithoutCategoryIds()
                .setTitle("Test title 2")
                .setAuthor("Test author 2")
                .setPrice(new BigDecimal("5.45"))
                .setIsbn("9783161484101")
                .setDescription("Test description 2")
                .setCoverImage("http://example2.com/cover.jpg");

        return List.of(bookDtoWithoutCategoryIds1,
                    bookDtoWithoutCategoryIds2);
    }

    private Book createTestUpdatedBook() {
        Book updatedBook = createTestBook();

        updatedBook.setTitle("Updated title");
        updatedBook.setAuthor("Updated author");
        updatedBook.setPrice(new BigDecimal("4.99"));
        updatedBook.setDescription("Updated description");

        return updatedBook;
    }

    private Set<Category> createTestCategorySet() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Non-Fiction");

        return Set.of(category1, category2);
    }
}
