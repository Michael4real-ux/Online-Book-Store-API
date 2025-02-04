package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart API", description = "Cart API endpoints")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ShoppingCart createCart() {
        return cartService.createCart();
    }

    @PostMapping("/{cartId}/add")
    @Operation(summary = "Add books to cart")
    public ShoppingCart addBookToCart(@PathVariable Long cartId, @RequestParam Long bookId) {
        return cartService.addBookToCart(cartId, bookId);
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart items")
    public ShoppingCart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }
}
