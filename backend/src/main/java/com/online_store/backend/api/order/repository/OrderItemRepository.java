package com.online_store.backend.api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.order.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
