package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.model.PaymentMethod; // Import the enum
import com.dammy.bookstoreapi.repository.PurchaseRepository;
import com.dammy.bookstoreapi.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CheckoutServiceTest {

    @InjectMocks
    private CheckoutService checkoutService;

    @Mock
    private PurchaseRepository purchaseRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckout() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.addBook(new Book());

        PaymentMethod paymentMethod = PaymentMethod.WEB;

        Purchase purchase = new Purchase();
        purchase.setCartId(String.valueOf(shoppingCart.getId()));
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentMethod(paymentMethod);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        // Act
        Purchase result = checkoutService.checkout(shoppingCart, paymentMethod);

        // Assert
        assertNotNull(result);
        assertEquals(String.valueOf(shoppingCart.getId()), result.getCartId());
        assertNotNull(result.getPurchaseDate());
        assertEquals(paymentMethod, result.getPaymentMethod());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    public void testCheckout_NullShoppingCart() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(null, PaymentMethod.WEB);
        }, "Shopping cart cannot be empty");
    }

    @Test
    public void testCheckout_EmptyShoppingCart() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart(); // Empty cart

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(shoppingCart, PaymentMethod.WEB); // Should throw an exception for empty cart
        }, "Shopping cart cannot be empty");
    }

    @Test
    public void testCheckout_NullPaymentMethod() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.addBook(new Book());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(shoppingCart, null); // Null payment method should throw an exception
        }, "Payment method is required");
    }

    @Test
    public void testCheckout_PurchaseRepositorySaveFailure() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.addBook(new Book());

        PaymentMethod paymentMethod = PaymentMethod.WEB;

        when(purchaseRepository.save(any(Purchase.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            checkoutService.checkout(shoppingCart, paymentMethod); // Should throw an exception if save fails
        }, "Database error");
    }
}