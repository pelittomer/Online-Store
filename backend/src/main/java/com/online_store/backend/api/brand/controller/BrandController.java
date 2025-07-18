package com.online_store.backend.api.brand.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.brand.service.BrandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public String addBrand(@RequestBody String brandDetails) { // Likely a BrandDto
        // This function adds a new brand.
        // 'brandDetails' would typically contain information like the brand's name.
        return "New brand added: " + brandDetails;
    }

    @GetMapping
    public String listBrands() {
        // This function retrieves and lists all available brands.
        // It provides a comprehensive list of all brands in the system.
        return "List of brands will be returned here.";
    }
}
