package com.online_store.backend.api.brand.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.brand.repository.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    
}
