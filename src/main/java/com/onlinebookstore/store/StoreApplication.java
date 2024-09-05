package com.onlinebookstore.store;

import com.onlinebookstore.store.model.Book;
import com.onlinebookstore.store.repository.BookRepositoryImpl;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreApplication {
    @Autowired
    private BookRepositoryImpl bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Effective Java");
            book.setAuthor("Joshua Bloch");
            book.setIsbn("978-0134685991");
            book.setPrice(new BigDecimal("45.99"));
            bookRepository.save(book);
            System.out.println(bookRepository.findAll());
        };
    }
}
