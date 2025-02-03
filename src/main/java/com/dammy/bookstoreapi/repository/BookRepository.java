package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByPublicationYear(Integer publicationYear);

    List<Book> findByGenreContainingIgnoreCase(String genre);
}