package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart API", description = "Cart API endpoints")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    @Operation(summary = "Add books to cart")
    @SecurityRequirement(name = "bearerAuth")
    public ShoppingCart addBooksToCart(@PathVariable Long userId, @RequestBody BookIdsRequest request) {
        List<Long> bookIds = request.getBookIds();
        return cartService.addBooksToCart(userId, bookIds);
    }

    @PostMapping("/{userId}/remove")
    @Operation(summary = "Remove books from cart")
    @SecurityRequirement(name = "bearerAuth")
    public ShoppingCart removeBooksFromCart(@PathVariable Long userId, @RequestBody BookIdsRequest request) {
        List<Long> bookIds = request.getBookIds();
        return cartService.removeBooksFromCart(userId, bookIds);
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart items")
    public ShoppingCart getCart(@PathVariable String cartId) {
        String[] parts = cartId.split(":");
        Long userId = Long.parseLong(parts[2]);
        return cartService.getCart(userId);
    }

    public static class BookIdsRequest {
        private List<Long> bookIds;

        public List<Long> getBookIds() {
            return bookIds;
        }

        public void setBookIds(List<Long> bookIds) {
            this.bookIds = bookIds;
        }
    }

}
