package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    // Add a book to the cart
    public ShoppingCart addBookToCart(Long userId, Long bookId) {
        logger.info("Adding book {} to cart for user {}", bookId, userId);
        String cartKey = "cart:user:" + userId;

        // Check if the cart exists; if not, create it
        if (!redisTemplate.hasKey(cartKey)) {
            createCart(userId);
        }

        // Fetch the book and add it to the cart
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Retrieve the cart from Redis
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            cart = new ShoppingCart();
            cart.setId(cartKey);
        }

        // Add the book to the cart
        cart.addBook(book);

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
    public ShoppingCart removeBookFromCart(Long userId, Long bookId) {
        String cartKey = "cart:user:" + userId;
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        // Remove the book from the cart
        cart.removeBook(bookId);

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