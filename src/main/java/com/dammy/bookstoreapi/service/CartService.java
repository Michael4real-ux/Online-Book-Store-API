package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        return cartRepository.save(cart);
    }

    public ShoppingCart addBookToCart(Long cartId, Book book) {
        ShoppingCart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getBooks().add(book);
        return cartRepository.save(cart);
    }

    public Optional<ShoppingCart> getCart(Long cartId) {
        return cartRepository.findById(cartId);
    }
}