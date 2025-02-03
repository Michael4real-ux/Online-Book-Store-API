package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.CartRepository;
import com.dammy.bookstoreapi.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

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
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);
        Book book = new Book();
        book.setTitle("Book 1");

        ShoppingCart updatedCart = new ShoppingCart();
        updatedCart.setId(cartId);
        updatedCart.getBooks().add(book);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(updatedCart);

        ShoppingCart result = cartService.addBookToCart(cartId, book);
        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(1, result.getBooks().size());
        assertEquals("Book 1", result.getBooks().get(0).getTitle());
        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddBookToCart_CartNotFound() {
        Long cartId = 1L;
        Book book = new Book();
        book.setTitle("Book 1");

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.addBookToCart(cartId, book));
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    public void testGetCart() {
        Long cartId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Optional<ShoppingCart> foundCart = cartService.getCart(cartId);
        assertTrue(foundCart.isPresent());
        assertEquals(cartId, foundCart.get().getId());
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    public void testGetCart_CartNotFound() {
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        Optional<ShoppingCart> foundCart = cartService.getCart(cartId);
        assertFalse(foundCart.isPresent());
        verify(cartRepository, times(1)).findById(cartId);
    }
}