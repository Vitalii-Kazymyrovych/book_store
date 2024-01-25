package com.project.bookstore;

import com.project.bookstore.config.AppConfig;
import com.project.bookstore.model.Book;
import com.project.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfig.class);

        Book newBook = new Book();
        newBook.setAuthor("Stephen King");
        newBook.setTitle("It");
        newBook.setDescription("Scary and uncanny");
        newBook.setIsbn("123435");
        newBook.setPrice(BigDecimal.valueOf(1000L));
        newBook.setCoverImage("http");

        BookService bookService = context.getBean(BookService.class);
        bookService.save(newBook);
        bookService.findAll().forEach(System.out::println);
        SpringApplication.run(BookStoreApplication.class, args);
    }
}
