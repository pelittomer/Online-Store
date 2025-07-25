package com.online_store.backend.api.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.user.entities.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
