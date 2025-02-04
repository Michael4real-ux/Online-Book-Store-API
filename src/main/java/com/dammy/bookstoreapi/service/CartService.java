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

    public ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        redisTemplate.opsForValue().set("cart:" + cart.getId(), cart);
        return cart;
    }

    public ShoppingCart addBookToCart(Long cartId, Long bookId) {
        ShoppingCart cart = redisTemplate.opsForValue().get("cart:" + cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        cart.addBook(book);
        redisTemplate.opsForValue().set("cart:" + cartId, cart);
        return cart;
    }

    public ShoppingCart getCart(Long cartId) {
        return redisTemplate.opsForValue().get("cart:" + cartId);
    }
}