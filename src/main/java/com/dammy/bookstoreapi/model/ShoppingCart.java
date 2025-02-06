package com.dammy.bookstoreapi.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCart implements Serializable {
    private String id;
    private Set<Book> books = new HashSet<>();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Add a book to the cart
    public void addBook(Book book) {
        books.add(book);
    }

    // Remove a book from the cart
    public void removeBook(Long bookId) {
        books.removeIf(book -> book.getId().equals(bookId));
    }
}