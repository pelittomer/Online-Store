package com.online_store.backend.api.cart.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    
}
