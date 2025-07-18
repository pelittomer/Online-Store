package com.online_store.backend.api.variation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.variation.service.VariationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variation")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService variationService;

    @PostMapping
    public String addVariation(@RequestBody String variationDetails) { // Likely a VariationDto
        // This function adds a new product variation.
        // 'variationDetails' would typically include properties like size, color, or
        // material.
        return "New variation added: " + variationDetails;
    }

    @GetMapping
    public String listVariations() {
        // This function retrieves and lists all available product variations.
        // It provides a comprehensive list of all defined variations in the system.
        return "List of variations will be returned here.";
    }
}
