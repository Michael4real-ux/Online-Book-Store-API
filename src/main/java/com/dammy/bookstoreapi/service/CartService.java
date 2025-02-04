package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisTemplate<String, ShoppingCart> redisTemplate;

    // Create a new shopping cart
    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(System.currentTimeMillis()); // Unique ID generation
        redisTemplate.opsForValue().set("cart:" + cart.getId(), cart);
        return cart;
    }

    // Add a book to the cart
    public ShoppingCart addBookToCart(Long cartId, Long bookId) {
        ShoppingCart cart = redisTemplate.opsForValue().get("cart:" + cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        cart.addBook(book);
        redisTemplate.opsForValue().set("cart:" + cartId, cart);
        return cart;
    }

    // Remove a book from the cart
    public ShoppingCart removeBookFromCart(Long cartId, Long bookId) {
        ShoppingCart cart = redisTemplate.opsForValue().get("cart:" + cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        cart.removeBook(bookId);
        redisTemplate.opsForValue().set("cart:" + cartId, cart);
        return cart;
    }

    // Get the contents of the cart
    public ShoppingCart getCart(Long cartId) {
        ShoppingCart cart = redisTemplate.opsForValue().get("cart:" + cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        return cart;
    }

    // Clear the cart
    public void clearCart(Long cartId) {
        redisTemplate.delete("cart:" + cartId);
    }
}