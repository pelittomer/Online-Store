package com.online_store.backend.api.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.product.dto.request.ProductRequestDto;
import com.online_store.backend.api.product.dto.response.ProductDetailsResponseDto;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.service.ProductService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
        private final ProductService productService;

        @PostMapping
        public ResponseEntity<ApiResponse<String>> addProduct(
                        @Valid @RequestPart("product") ProductRequestDto productRequestDto,
                        MultipartHttpServletRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.success(productService.addProduct(productRequestDto, request)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> updateProduct(
                        @PathVariable("id") Long id,
                        MultipartHttpServletRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.success("Update product."));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<ProductResponseDto>>> listProducts() {
                return ResponseEntity.ok(
                                ApiResponse.success(productService.listProducts()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ProductDetailsResponseDto>> getProductById(
                        @PathVariable(required = true) Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success(productService.getProductById(id)));
        }
}