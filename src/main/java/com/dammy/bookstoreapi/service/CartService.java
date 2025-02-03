package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.CartRepository;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        return cartRepository.save(cart);
    }

    public ShoppingCart addBookToCart(Long cartId, Long bookId) {
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!cart.getBooks().contains(book)) {
            cart.getBooks().add(book);
        }

        return cartRepository.save(cart);
    }

    public ShoppingCart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseGet(ShoppingCart::new);
    }
}
