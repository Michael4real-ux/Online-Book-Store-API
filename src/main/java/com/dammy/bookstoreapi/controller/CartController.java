package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart API", description = "Cart API endpoints")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    @Operation(summary = "Add books to cart")
    @SecurityRequirement(name = "bearerAuth")
    public ShoppingCart addBookToCart(@PathVariable Long userId, @RequestParam Long bookId) {
        return cartService.addBookToCart(userId, bookId);
    }


    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart items")
    public ShoppingCart getCart(@PathVariable Long cartId) {
        return cartService.getCart(cartId);
    }
}
