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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
    private SetOperations<String, Object> setOperations;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

//    @Test
//    public void testAddBookToCart() {
//        Long userId = 1L;
//        Long bookId = 2L;
//        String cartKey = "cart:user:" + userId;
//
//        Book book = new Book();
//        book.setId(bookId);
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//        when(redisTemplate.hasKey(cartKey)).thenReturn(true);
//
//        Set<Object> bookIds = new HashSet<>();
//        bookIds.add(bookId.toString());
//
//        when(setOperations.members(cartKey)).thenReturn(bookIds);
//
//        ShoppingCart resultCart = cartService.addBookToCart(userId, bookId);
//
//        assertNotNull(resultCart);
//        assertEquals(cartKey, resultCart.getId());
//        assertEquals(1, resultCart.getBooks().size());
//
//        verify(setOperations, times(1)).add(cartKey, bookId.toString());
//    }

//    @Test
//    public void testAddBookToCart_BookNotFound() {
//        Long userId = 1L;
//        Long bookId = 2L;
//        String cartKey = "cart:user:" + userId;
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
//        when(redisTemplate.hasKey(cartKey)).thenReturn(true);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.addBookToCart(userId, bookId));
//        assertEquals("Book not found", exception.getMessage());
//
//        verify(bookRepository, times(1)).findById(bookId);
//        verify(setOperations, never()).add(anyString(), anyString());
//    }

    @Test
    public void testGetCart() {
        Long userId = 1L;
        String cartKey = "cart:user:" + userId;

        Set<Object> bookIds = new HashSet<>();
        bookIds.add("1");

        Book book = new Book();
        book.setId(1L);
        when(setOperations.members(cartKey)).thenReturn(bookIds);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ShoppingCart cart = cartService.getCart(userId);

        assertNotNull(cart);
        assertEquals(cartKey, cart.getId());
        assertEquals(1, cart.getBooks().size());

        verify(redisTemplate, times(1)).opsForSet();
        verify(setOperations, times(1)).members(cartKey);
    }

    @Test
    public void testClearCart() {
        Long userId = 1L;
        String cartKey = "cart:user:" + userId;

        cartService.clearCart(userId);

        verify(redisTemplate, times(1)).delete(cartKey);
    }
}
