package com.online_store.backend.api.variation.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.variation.repository.VariationOptionRepository;
import com.online_store.backend.api.variation.repository.VariationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationService {
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;
    
}
