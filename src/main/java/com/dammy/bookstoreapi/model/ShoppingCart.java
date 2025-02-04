package com.dammy.bookstoreapi.model;


import java.util.HashSet;
import java.util.Set;


public class ShoppingCart {

    private Long id;

    private Set<Book> books = new HashSet<>();

    public ShoppingCart() {
        this.books = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }


    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Long bookId) {
        this.books.removeIf(book -> book.getId().equals(bookId));
    }
}