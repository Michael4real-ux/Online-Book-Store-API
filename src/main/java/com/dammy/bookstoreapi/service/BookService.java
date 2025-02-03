package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> searchBooks(String title, String author, Integer year, String genre) {
        return bookRepository.findByTitleContainingOrAuthorContainingOrYearOfPublicationOrGenre(title, author, year, genre);
    }
}