package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublicationYear(Integer publicationYear);

    List<Book> findByGenreContainingIgnoreCase(String genre);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Book> findByAuthorName(@Param("name") String name);

    Book findByIsbn(String isbn); // Add this method

}