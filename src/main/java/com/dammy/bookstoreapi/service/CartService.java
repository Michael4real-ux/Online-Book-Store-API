package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.dto.BookDTO;
import com.dammy.bookstoreapi.dto.UserDTO;
import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.utils.BookQuantity;
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
    public ShoppingCart addBooksToCart(Long userId, List<BookQuantity> bookQuantities) {
        logger.info("Adding books to cart for user {}", userId);
        String cartKey = "cart:user:" + userId;

        // Check if the cart exists; if not, create it
        if (!redisTemplate.hasKey(cartKey)) {
            createCart(userId);
        }

        // Retrieve the cart from Redis
        ShoppingCart cart = (ShoppingCart) redisTemplate.opsForValue().get(cartKey);
        if (cart == null) {
            cart = new ShoppingCart(cartKey, userId);
        }

        // Add each book to the cart
        for (BookQuantity bookQuantity : bookQuantities) {
            Book book = bookRepository.findById(bookQuantity.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            BookDTO bookDTO = new BookDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getGenre(),
                    book.getIsbn(),
                    new UserDTO(
                            book.getAuthor().getId(),
                            book.getAuthor().getUsername(),
                            book.getAuthor().getName(),
                            book.getAuthor().getRole()
                    ),
                    book.getPublicationYear(),
                    book.getPrice()
            );
            int quantity = bookQuantity.getQuantity() != null ? bookQuantity.getQuantity() : 1;
            cart.addBook(bookDTO, quantity);
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
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            BookDTO bookDTO = new BookDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getGenre(),
                    book.getIsbn(),
                    new UserDTO(
                            book.getAuthor().getId(),
                            book.getAuthor().getUsername(),
                            book.getAuthor().getName(),
                            book.getAuthor().getRole()
                    ),
                    book.getPublicationYear(),
                    book.getPrice()
            );
            cart.removeBook(bookDTO);
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