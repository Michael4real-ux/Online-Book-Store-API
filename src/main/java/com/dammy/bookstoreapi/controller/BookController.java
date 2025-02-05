package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.dto.BookDTO;
import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book API", description = "Book API endpoints")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    public List<BookDTO> getAllBooks() {  // Change return type to List<BookDTO>
        return bookService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    @SecurityRequirement(name = "bearerAuth")
    public Book createBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title, author name, year, genre")
    public List<BookDTO> searchBooks(@RequestParam(required = false) String title,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) Integer year,
                                     @RequestParam(required = false) String genre) {
        return bookService.searchBooks(title, author, year, genre);
    }
}
