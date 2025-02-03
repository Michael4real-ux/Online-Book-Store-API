package com.dammy.bookstoreapi;

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

        // You can define a PaymentMethod (for example, Web)
        PaymentMethod paymentMethod = PaymentMethod.WEB;

        Purchase purchase = new Purchase();
        purchase.setShoppingCart(shoppingCart);
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentMethod(paymentMethod);

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        // Act
        Purchase result = checkoutService.checkout(shoppingCart, paymentMethod);

        // Assert
        assertNotNull(result);
        assertEquals(shoppingCart, result.getShoppingCart());
        assertNotNull(result.getPurchaseDate());
        assertEquals(paymentMethod, result.getPaymentMethod()); // Check that the payment method is set
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    public void testCheckout_NullShoppingCart() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(null, PaymentMethod.WEB); // Now requires a paymentMethod
        }, "Shopping cart cannot be empty");
    }

    @Test
    public void testCheckout_NullPaymentMethod() {
        // Arrange
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(shoppingCart, null); // Null payment method should throw an exception
        }, "Payment method is required");
    }
}
