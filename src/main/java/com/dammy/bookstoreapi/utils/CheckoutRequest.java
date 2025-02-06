package com.dammy.bookstoreapi.utils;

import com.dammy.bookstoreapi.model.PaymentMethod;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CheckoutRequest {
    private ShoppingCart shoppingCart;



    @JsonDeserialize(using = PaymentMethodDeserializer.class)
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

