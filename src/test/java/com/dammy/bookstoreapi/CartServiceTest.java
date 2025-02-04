package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testCreateCart() {
        ShoppingCart cart = cartService.createCart();
        assertNotNull(cart);

        verify(valueOperations, times(1)).set(anyString(), any(ShoppingCart.class));
    }

    @Test
    public void testAddBookToCart() {
        String cartId = "1";
        Long bookId = 2L;

        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(redisTemplate.hasKey("cart:" + cartId)).thenReturn(true);

        Map<Object, Object> entries = new HashMap<>();
        entries.put(bookId.toString(), book);

        when(hashOperations.entries("cart:" + cartId)).thenReturn(entries);

        ShoppingCart resultCart = cartService.addBookToCart(Long.parseLong(cartId), bookId);

        assertNotNull(resultCart);
        assertEquals(cartId, resultCart.getId());
        assertEquals(1, resultCart.getBooks().size());

        verify(hashOperations, times(1)).put("cart:" + cartId, bookId.toString(), book);
    }

    @Test
    public void testAddBookToCart_BookNotFound() {
        String cartId = "1";
        Long bookId = 2L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        when(redisTemplate.hasKey("cart:" + cartId)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.addBookToCart(Long.parseLong(cartId), bookId));
        assertEquals("Book not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(hashOperations, never()).put(anyString(), anyString(), any());
    }

    @Test
    public void testGetCart() {
        String cartId = "1";

        Map<Object, Object> entries = new HashMap<>();
        Book book = new Book();
        book.setId(1L);
        entries.put("book:1", book);
        when(hashOperations.entries("cart:" + cartId)).thenReturn(entries);

        ShoppingCart cart = cartService.getCart(Long.parseLong(cartId));

        assertNotNull(cart);
        assertEquals(cartId, cart.getId());
        assertEquals(1, cart.getBooks().size());

        verify(redisTemplate, times(1)).opsForHash();
        verify(hashOperations, times(1)).entries("cart:" + cartId);
    }

    @Test
    public void testClearCart() {
        Long cartId = 1L;

        cartService.clearCart(cartId);

        verify(redisTemplate, times(1)).delete("cart:" + cartId);
    }
}