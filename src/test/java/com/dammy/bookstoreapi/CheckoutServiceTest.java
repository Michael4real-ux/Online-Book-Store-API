package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.model.ShoppingCart;
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
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        Purchase purchase = new Purchase();
        purchase.setShoppingCart(shoppingCart);
        purchase.setPurchaseDate(new Date());

        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        Purchase result = checkoutService.checkout(shoppingCart);
        assertNotNull(result);
        assertEquals(shoppingCart, result.getShoppingCart());
        assertNotNull(result.getPurchaseDate());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    public void testCheckout_NullShoppingCart() {
        assertThrows(NullPointerException.class, () -> {
            checkoutService.checkout(null);
        }, "java.lang.NullPointerException");
    }
}