package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.CartRepository;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCart() {
        ShoppingCart cart = new ShoppingCart();
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart createdCart = cartService.createCart();
        assertNotNull(createdCart);
        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddBookToCart() {
        Long cartId = 1L;
        Long bookId = 2L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);
        cart.setBooks(new ArrayList<>()); // Ensure books list is initialized

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book 1");

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ShoppingCart result = cartService.addBookToCart(cartId, bookId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(1, result.getBooks().size());
        assertEquals("Book 1", result.getBooks().get(0).getTitle());

        verify(cartRepository, times(1)).findById(cartId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddBookToCart_CartNotFound() {
        Long cartId = 1L;
        Long bookId = 2L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addBookToCart(cartId, bookId));

        verify(cartRepository, times(1)).findById(cartId);
        verify(bookRepository, never()).findById(anyLong());
    }

    @Test
    public void testAddBookToCart_BookNotFound() {
        Long cartId = 1L;
        Long bookId = 2L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);
        cart.setBooks(new ArrayList<>());

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addBookToCart(cartId, bookId));

        verify(cartRepository, times(1)).findById(cartId);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testGetCart() {
        Long cartId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        ShoppingCart foundCart = cartService.getCart(cartId);
        assertNotNull(foundCart);
        assertEquals(cartId, foundCart.getId());

        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    public void testGetCart_CartNotFound() {
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        ShoppingCart foundCart = cartService.getCart(cartId);
        assertNotNull(foundCart); // Now returns a new empty cart instead of Optional.empty()
        assertEquals(0, foundCart.getBooks().size()); // Ensure it's an empty cart

        verify(cartRepository, times(1)).findById(cartId);
    }
}
