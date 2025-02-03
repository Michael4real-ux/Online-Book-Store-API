package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.model.ShoppingCart;
import com.dammy.bookstoreapi.model.Purchase;
import com.dammy.bookstoreapi.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CheckoutService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase checkout(ShoppingCart cart) {
        Purchase purchase = new Purchase();
        purchase.setCart(cart);
        purchase.setPurchaseDate(new Date());
        return purchaseRepository.save(purchase);
    }
}