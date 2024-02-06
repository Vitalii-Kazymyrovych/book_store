package com.project.bookstore.repository;

import com.project.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET "
            + "b.title = :#{#book.title}, "
            + "b.author = :#{#book.author}, "
            + "b.price = :#{#book.price}, "
            + "b.description = :#{#book.description}, "
            + "b.coverImage = :#{#book.coverImage} "
            + "WHERE b.id = :#{#book.id}")
    void update(@Param("book") Book book);
}
