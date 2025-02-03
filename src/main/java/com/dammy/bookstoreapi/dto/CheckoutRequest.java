package com.dammy.bookstoreapi.dto;

import com.dammy.bookstoreapi.model.PaymentMethod;
import com.dammy.bookstoreapi.model.ShoppingCart;

public class CheckoutRequest {
    private ShoppingCart shoppingCart;
    private PaymentMethod paymentMethod;

    // Getters and Setters
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

