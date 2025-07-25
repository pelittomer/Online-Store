package com.online_store.backend.api.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.cart.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
