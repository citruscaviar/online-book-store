package com.onlinebookstore.store.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;

import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.model.Category;
import com.onlinebookstore.store.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Find books by existed category id")
@Sql(scripts = {
        "classpath:database/test/books/add-books-to-table.sql",
        "classpath:database/test/categories/add-categories-to-table.sql",
        "classpath:database/test/books/add-books-and-categories-into-table.sql",
},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/test/books/delete-books-categories.sql",
        "classpath:database/test/books/remove-books-from-table-books.sql",
        "classpath:database/test/categories/delete-categories.sql",
},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void findAllByCategoryId_ExistedId_Success() {
        Category category = new Category();
        category.setId(12L);
        category.setName("Test a");
        Book firstBook = new Book();
        firstBook.setId(12L);
        firstBook.setTitle("Test Book 1");
        firstBook.setAuthor("Test Author 1");
        firstBook.setIsbn("9783161484100");
        firstBook.setPrice(BigDecimal.valueOf(19.99));
        firstBook.setDescription("Description for Test Book 1");
        firstBook.setCoverImage("http://example.com/cover1.jpg");
        firstBook.setCategories(Set.of(category));

        Book secondBook = new Book();
        secondBook.setId(13L);
        secondBook.setTitle("Test Book 2");
        secondBook.setAuthor("Test Author 2");
        secondBook.setIsbn("9783161484101");
        secondBook.setPrice(BigDecimal.valueOf(29.99));
        secondBook.setDescription("Description for Test Book 2");
        secondBook.setCoverImage("http://example.com/cover2.jpg");
        secondBook.setCategories(Set.of(category));

        List<Book> expected = List.of(firstBook, secondBook);

        List<Book> actual = bookRepository.findAllByCategoryId(12L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
