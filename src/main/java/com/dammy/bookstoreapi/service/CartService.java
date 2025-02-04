package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Create a new shopping cart
    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(UUID.randomUUID().toString()); // Unique ID generation
        redisTemplate.opsForValue().set("cart:" + cart.getId(), cart);
        return cart;
    }

    // Add a book to the cart
    public ShoppingCart addBookToCart(Long cartId, Long bookId) {
        String cartKey = "cart:" + cartId;
        if (!redisTemplate.hasKey(cartKey)) {
            throw new RuntimeException("Cart not found");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        // Store bookId as a field in the Redis Hash
        redisTemplate.opsForHash().put(cartKey, bookId.toString(), book); // Use bookId as field and Book object as value

        return getCartFromRedis(cartId); // Return the updated cart
    }

    // Get cart from Redis
    private ShoppingCart getCartFromRedis(Long cartId) {
        String cartKey = "cart:" + cartId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId.toString());
        if (!CollectionUtils.isEmpty(entries)) {
            Collection<Object> books = entries.values();
            books.forEach(book -> {
                if (book instanceof Book) {
                    cart.addBook((Book) book);
                }
            });
        }
        return cart;
    }

    // Remove a book from the cart
    public ShoppingCart removeBookFromCart(Long cartId, Long bookId) {
        String cartKey = "cart:" + cartId;
        if (!redisTemplate.hasKey(cartKey)) {
            throw new RuntimeException("Cart not found");
        }

        redisTemplate.opsForHash().delete(cartKey, bookId.toString()); // Remove the book entry from the Hash
        return getCartFromRedis(cartId); // Update and return the cart
    }

    // Get the contents of the cart
    public ShoppingCart getCart(Long cartId) {
        return getCartFromRedis(cartId);
    }

    // Clear the cart
    public void clearCart(Long cartId) {
        redisTemplate.delete("cart:" + cartId);
    }
}