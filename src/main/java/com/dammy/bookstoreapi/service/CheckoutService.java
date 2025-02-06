package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.utils.CheckoutRequest;
import com.dammy.bookstoreapi.model.PaymentMethod;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public double calculateTotalAmount(CheckoutRequest request) {
        ShoppingCart shoppingCart = request.getShoppingCart();
        return shoppingCart.calculateTotalAmount();
    }

    public Purchase checkout(CheckoutRequest request) {
        ShoppingCart shoppingCart = request.getShoppingCart();
        PaymentMethod paymentMethod = request.getPaymentMethod();

        if (shoppingCart == null || shoppingCart.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart cannot be empty");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method is required");
        }

        double totalAmount = calculateTotalAmount(request);

        // Simulate payment process
        boolean paymentSuccessful = simulatePayment(paymentMethod, totalAmount);

        if (!paymentSuccessful) {
            throw new RuntimeException("Payment failed");
        }

        Purchase purchase = new Purchase();
        purchase.setCartId(shoppingCart.getId());
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentMethod(paymentMethod);
        purchase.setTotalAmount(totalAmount);

        return purchaseRepository.save(purchase);
    }

    private boolean simulatePayment(PaymentMethod paymentMethod, double amount) {
        // Simulating payment process (e.g., call a payment gateway API)
        // For demonstration purposes,I will assume payment is always successful
        return true;
    }

    public List<Purchase> history(){
        return purchaseRepository.findAll();
    }
}
