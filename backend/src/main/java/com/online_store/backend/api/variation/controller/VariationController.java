package com.online_store.backend.api.variation.controller;

import java.util.List;

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

/**
 * REST controller for managing product variations.
 * This controller provides endpoints for creating new variations and
 * for retrieving a list of existing variations, optionally filtered by
 * category.
 */
@RestController
@RequestMapping("/api/variation")
@RequiredArgsConstructor
public class VariationController {
        private final VariationService variationService;

        /**
         * Endpoint to add a new variation.
         * This variation can be a global variation or specific to a category.
         *
         * @param variationRequestDto The DTO containing the variation's details.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addVariation(
                        @Valid @RequestBody VariationRequestDto variationRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                variationService.addVariation(variationRequestDto)));
        }

        /**
         * Endpoint to get a list of variations.
         * The list can be filtered by providing an optional category ID. If no category
         * ID is provided,
         * it will return a list of all global variations. If a category ID is provided,
         * it will return
         * both the global variations and the variations specific to that category.
         *
         * @param categoryId An optional ID of a category to filter the variations.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link VariationResponseDto}.
         */
        @GetMapping()
        public ResponseEntity<ApiResponse<List<VariationResponseDto>>> listVariations(
                        @RequestParam Long categoryId) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                variationService.listVariations(categoryId)));
        }
}
