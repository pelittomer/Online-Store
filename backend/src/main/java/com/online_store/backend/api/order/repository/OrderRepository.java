package com.online_store.backend.api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.order.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
