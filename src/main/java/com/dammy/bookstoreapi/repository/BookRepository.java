package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthorContainingOrYearOfPublicationOrGenre(String title, String author, int year, String genre);
}
