package com.online_store.backend.api.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.cart.entities.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {

}
