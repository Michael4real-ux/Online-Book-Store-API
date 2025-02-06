package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.dto.BookDTO;
import com.dammy.bookstoreapi.dto.UserDTO;
import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.model.PaymentMethod; // Import the enum
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.PurchaseRepository;
import com.dammy.bookstoreapi.service.CheckoutService;
import com.dammy.bookstoreapi.utils.CheckoutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CheckoutServiceTest {


    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckout() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId("1L");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setGenre("Book Genre");
        book.setIsbn("Book ISBN");
        book.setAuthor(new User());
        book.setPublicationYear(2022);
        book.setPrice(19.99);

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

        shoppingCart.addBook(bookDTO, 1);

        PaymentMethod paymentMethod = PaymentMethod.WEB;

        CheckoutRequest request = new CheckoutRequest();
        request.setShoppingCart(shoppingCart);
        request.setPaymentMethod(paymentMethod);

        Purchase purchase = new Purchase();
        purchase.setCartId(String.valueOf(shoppingCart.getId()));
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentMethod(paymentMethod);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        // Act
        Purchase result = checkoutService.checkout(request);

        // Assert
        assertNotNull(result);
        assertEquals(String.valueOf(shoppingCart.getId()), result.getCartId());
        assertNotNull(result.getPurchaseDate());
        assertEquals(paymentMethod, result.getPaymentMethod());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    public void testCheckout_NullShoppingCart() {
        // Arrange
        PaymentMethod paymentMethod = PaymentMethod.WEB;

        CheckoutRequest request = new CheckoutRequest();
        request.setPaymentMethod(paymentMethod);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(request);
        }, "Shopping cart cannot be empty");
    }

    @Test
    public void testCheckout_EmptyShoppingCart() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart(); // Empty cart

        PaymentMethod paymentMethod = PaymentMethod.WEB;

        CheckoutRequest request = new CheckoutRequest();
        request.setShoppingCart(shoppingCart);
        request.setPaymentMethod(paymentMethod);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(request); // Should throw an exception for empty cart
        }, "Shopping cart cannot be empty");
    }

    @Test
    public void testCheckout_PurchaseRepositorySaveFailure() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId("1L");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");
        book.setGenre("Book Genre");
        book.setIsbn("Book ISBN");
        book.setAuthor(new User());
        book.setPublicationYear(2022);
        book.setPrice(19.99);

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

        shoppingCart.addBook(bookDTO, 1);

        PaymentMethod paymentMethod = PaymentMethod.WEB;

        CheckoutRequest request = new CheckoutRequest();
        request.setShoppingCart(shoppingCart);
        request.setPaymentMethod(paymentMethod);

        when(purchaseRepository.save(any(Purchase.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            checkoutService.checkout(request); // Should throw an exception if save fails
        }, "Database error");
    }
}