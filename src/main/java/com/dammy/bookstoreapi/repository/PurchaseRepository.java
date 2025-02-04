package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}