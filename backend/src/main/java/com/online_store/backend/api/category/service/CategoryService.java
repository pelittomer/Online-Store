package com.online_store.backend.api.category.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
}
