package com.online_store.backend.api.variation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.service.VariationService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variation")
@RequiredArgsConstructor
public class VariationController {
    private final VariationService variationService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addVariation(
            @Valid @RequestBody VariationRequestDto variationRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(variationService.addVariation(variationRequestDto)));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<VariationResponseDto>>> listVariations(
            @RequestParam Optional<Long> categoryId) {
        return ResponseEntity.ok(
                ApiResponse.success(variationService.listVariations(categoryId)));
    }
}
