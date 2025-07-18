package com.online_store.backend.api.product.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    

}