package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooks(String title, String author, Integer year, String genre) {
        List<Book> result = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            result.addAll(bookRepository.findByTitleContainingIgnoreCase(title));
        }
        if (author != null && !author.isEmpty()) {
            result.addAll(bookRepository.findByAuthorContainingIgnoreCase(author));
        }
        if (year != null) {
            result.addAll(bookRepository.findByPublicationYear(year));
        }
        if (genre != null && !genre.isEmpty()) {
            result.addAll(bookRepository.findByGenreContainingIgnoreCase(genre));
        }

        // Return distinct books
        return result.stream().distinct().toList();
    }
}