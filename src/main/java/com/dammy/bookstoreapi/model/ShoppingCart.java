package com.dammy.bookstoreapi.model;

import com.dammy.bookstoreapi.dto.BookDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCart implements Serializable {
    private String id;
    private Long userId;
    private Set<BookDTO> books = new HashSet<>();

    public ShoppingCart() {
    }

    public ShoppingCart(String id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDTO> books) {
        this.books = books;
    }

    // Add a book to the cart
    public void addBook(BookDTO book, int quantity) {
        // Check if the book already exists in the cart
        for (BookDTO cartBook : books) {
            if (cartBook.id().equals(book.id())) {
                // Update the quantity if the book already exists
                return;
            }
        }
        // Add the book to the cart if it doesn't exist
        books.add(book);
    }

    // Remove a book from the cart
    public void removeBook(BookDTO book) {
        books.removeIf(b -> b.id().equals(book.id()));
    }

    // Calculate total amount
    public double calculateTotalAmount() {
        double totalAmount = 0;
        for (BookDTO book : books) {
            totalAmount += book.price() * getQuantity(book);
        }
        return totalAmount;
    }

    // Get quantity of a book in the cart
    private int getQuantity(BookDTO book) {
        int quantity = 0;
        for (BookDTO b : books) {
            if (b.id().equals(book.id())) {
                quantity++;
            }
        }
        return quantity;
    }
}