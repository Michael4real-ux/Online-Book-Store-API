package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Book;
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

    public Purchase checkout(ShoppingCart shoppingCart, PaymentMethod paymentMethod) {
        if (shoppingCart == null || shoppingCart.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart cannot be empty");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
        Purchase purchase = new Purchase();
        purchase.setShoppingCart(shoppingCart);
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentMethod(paymentMethod);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> history(){
        return purchaseRepository.findAll();
    }


}