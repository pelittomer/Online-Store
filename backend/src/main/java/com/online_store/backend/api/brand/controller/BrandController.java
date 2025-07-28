package com.online_store.backend.api.brand.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.service.BrandService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing brand-related operations.
 * Provides endpoints for creating and retrieving brand information.
 */
@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {
        private final BrandService brandService;

        /**
         * Endpoint for creating a new brand.
         * This endpoint accepts brand details as a JSON object and a logo image file.
         *
         * @param brandRequestDto The brand details in a DTO.
         * @param file            The logo image file for the brand.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addBrand(
                        @Valid @RequestPart("brand") BrandRequestDto brandRequestDto,
                        @RequestPart(value = "file") MultipartFile file) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                brandService.addBrand(brandRequestDto, file)));
        }

        /**
         * Endpoint for retrieving a list of all brands.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link BrandResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<BrandResponseDto>>> listBrands() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                brandService.listBrands()));
        }
}
