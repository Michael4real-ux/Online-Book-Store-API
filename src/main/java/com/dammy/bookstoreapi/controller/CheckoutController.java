package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public Purchase checkout(@RequestBody ShoppingCart cart) {
        return checkoutService.checkout(cart);
    }
}