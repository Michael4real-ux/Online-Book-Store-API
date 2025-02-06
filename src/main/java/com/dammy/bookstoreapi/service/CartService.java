package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    // Create a new shopping cart
    public void createCart(Long userId) {
        String cartKey = "cart:user:" + userId;

        if (redisTemplate.hasKey(cartKey)) {
            return;  // Return if the cart already exists
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartKey);  // Set the cart ID
        redisTemplate.opsForValue().set(cartKey, cart);  // Store the cart in Redis
    }

    // Add books to the cart
    public ShoppingCart addBooksToCart(Long userId, List<Long> bookIds) {
        logger.info("Adding books to cart for user {}", userId);
        String cartKey = "cart:user:" + userId;

        // Check if the cart exists; if not, create it
        if (!redisTemplate.hasKey(cartKey)) {
            createCart(userId);
        }

        // Retrieve the cart from Redis
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setId(cartKey);
        }

        // Add each book to the cart if it's not already in the cart
        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            if (!cart.getBooks().contains(book)) {
                cart.addBook(book);
            }
        }

        // Save the updated cart back to Redis
        redisTemplate.opsForValue().set(cartKey, cart);

        return cart;
    }

    // Get cart from Redis
    public ShoppingCart getCart(Long userId) {
        String cartKey = "cart:user:" + userId;
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        return cart;
    }

    // Remove a book from the cart
    public ShoppingCart removeBooksFromCart(Long userId, List<Long> bookIds) {
        String cartKey = "cart:user:" + userId;

        // Retrieve the cart from Redis
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        // Remove each book from the cart
        for (Long bookId : bookIds) {
            cart.removeBook(bookId);
        }

        // Save the updated cart back to Redis
        redisTemplate.opsForValue().set(cartKey, cart);

        return cart;
    }

    // Clear the cart
    public void clearCart(Long userId) {
        String cartKey = "cart:user:" + userId;
        redisTemplate.delete(cartKey);
    }
}