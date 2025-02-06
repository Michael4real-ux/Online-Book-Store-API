package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.service.CartService;
import com.dammy.bookstoreapi.utils.BookIdsRequest;
import com.dammy.bookstoreapi.utils.BookQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

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
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testAddBookToCart() {
        Long userId = 1L;
        Long bookId = 2L;
        String cartKey = "cart:user:" + userId;

        // Mock book
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor(new User()); // Set the author to avoid NullPointerException

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(redisTemplate.hasKey(cartKey)).thenReturn(true);

        // Mock Redis cart
        ShoppingCart cart = new ShoppingCart(cartKey, userId);
        when(valueOperations.get(cartKey)).thenReturn(cart);

        // Prepare request
        BookIdsRequest request = new BookIdsRequest();
        List<BookQuantity> bookQuantities = new ArrayList<>();
        BookQuantity bookQuantity = new BookQuantity();
        bookQuantity.setBookId(bookId);
        bookQuantities.add(bookQuantity);
        request.setBookQuantities(bookQuantities);

        // Call the method
        ShoppingCart resultCart = cartService.addBooksToCart(userId, request.getBookQuantities());

        // Verify
        assertNotNull(resultCart);
        assertEquals(cartKey, resultCart.getId());
        assertEquals(1, resultCart.getBooks().size());

        // Verify Redis interactions
        verify(valueOperations, times(1)).get(cartKey);
        verify(valueOperations, times(1)).set(cartKey, cart);
    }

    @Test
    public void testAddBookToCart_BookNotFound() {
        Long userId = 1L;
        Long bookId = 2L;
        String cartKey = "cart:user:" + userId;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        when(redisTemplate.hasKey(cartKey)).thenReturn(true);

        BookIdsRequest request = new BookIdsRequest();
        List<BookQuantity> bookQuantities = new ArrayList<>();
        BookQuantity bookQuantity = new BookQuantity();
        bookQuantity.setBookId(bookId);
        bookQuantities.add(bookQuantity);
        request.setBookQuantities(bookQuantities);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.addBooksToCart(userId, request.getBookQuantities()));
        assertEquals("Book not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(bookId);
        verify(valueOperations, never()).set(anyString(), any());
    }

    @Test
    public void testGetCart() {
        Long userId = 1L;
        String cartKey = "cart:user:" + userId;

        // Mock Redis cart
        ShoppingCart cart = new ShoppingCart(cartKey, userId);
        when(valueOperations.get(cartKey)).thenReturn(cart);

        // Call the method
        ShoppingCart resultCart = cartService.getCart(userId);

        // Verify
        assertNotNull(resultCart);
        assertEquals(cartKey, resultCart.getId());

        // Verify Redis interactions
        verify(valueOperations, times(1)).get(cartKey);
    }

    @Test
    public void testGetCart_CartNotFound() {
        Long userId = 1L;
        String cartKey = "cart:user:" + userId;

        when(valueOperations.get(cartKey)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cartService.getCart(userId));
        assertEquals("Cart not found", exception.getMessage());

        verify(valueOperations, times(1)).get(cartKey);
    }

    @Test
    public void testClearCart() {
        Long userId = 1L;
        String cartKey = "cart:user:" + userId;

        cartService.clearCart(userId);

        verify(redisTemplate, times(1)).delete(cartKey);
    }
}