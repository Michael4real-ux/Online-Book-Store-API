package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
}