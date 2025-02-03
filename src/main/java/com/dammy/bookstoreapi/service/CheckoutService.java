package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CheckoutService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase checkout(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            throw new NullPointerException("Shopping cart cannot be null");
        }
        Purchase purchase = new Purchase();
        purchase.setShoppingCart(shoppingCart);
        purchase.setPurchaseDate(new Date());
        return purchaseRepository.save(purchase);
    }
}