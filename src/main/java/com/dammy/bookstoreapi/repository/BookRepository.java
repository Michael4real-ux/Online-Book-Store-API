package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublicationYear(Integer publicationYear);

    List<Book> findByGenreContainingIgnoreCase(String genre);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Book> findByAuthorName(@Param("name") String name);

    Book findByIsbn(String isbn);
}