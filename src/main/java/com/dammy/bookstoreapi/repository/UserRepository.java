package com.dammy.bookstoreapi.repository;

import com.dammy.bookstoreapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}