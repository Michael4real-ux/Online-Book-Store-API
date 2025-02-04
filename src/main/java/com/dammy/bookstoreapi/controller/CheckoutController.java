package com.dammy.bookstoreapi.controller;

import com.dammy.bookstoreapi.dto.CheckoutRequest;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/checkout")
@Tag(name = "Checkout API", description = "Checkout API endpoints")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    @Operation(summary = "Checkout process")
    @SecurityRequirement(name = "bearerAuth")
    public Purchase checkout(@RequestBody CheckoutRequest request) {
        // Call the CheckoutService with the shopping cart and payment method
        return checkoutService.checkout(request.getShoppingCart(), request.getPaymentMethod());
    }

    @GetMapping("/purchase/history")
    @Operation(summary = "Get all purchase history")
    @SecurityRequirement(name = "bearerAuth")
    public List<Purchase> getPurchaseHistory() {
        return checkoutService.history();
    }

}
