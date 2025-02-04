package com.dammy.bookstoreapi.model;


import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

@RedisHash("cart")
public class ShoppingCart {

    private String id;

    private Set<Book> books = new HashSet<>();

    public ShoppingCart() {
        this.books = new HashSet<>();
    }

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


    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Long bookId) {
        this.books.removeIf(book -> book.getId().equals(bookId));
    }
}