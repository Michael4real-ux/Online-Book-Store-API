package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) String genre) {
        return bookService.searchBooks(title, author, year, genre);
    }
}
