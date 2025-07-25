package com.online_store.backend.api.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.user.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
