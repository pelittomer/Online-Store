package com.online_store.backend.api.order.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    
}
