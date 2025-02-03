package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ShoppingCart createCart() {
        return cartService.createCart();
    }

    @PostMapping("/{cartId}/add")
    public ShoppingCart addBookToCart(@PathVariable Long cartId, @RequestParam Long bookId) {
        return cartService.addBookToCart(cartId, bookId);
    }

    @GetMapping("/{cartId}")
    public ShoppingCart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }
}
