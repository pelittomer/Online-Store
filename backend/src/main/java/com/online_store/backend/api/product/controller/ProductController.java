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

/**
 * REST controller for managing products.
 * This controller provides endpoints for sellers to add and update products,
 * as well as for all users to view product details and lists.
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
        private final ProductService productService;

        /**
         * Endpoint for a seller to add a new product.
         * The request includes product details and multipart files for images.
         *
         * @param productRequestDto The DTO containing product information.
         * @param request           The multipart request containing dynamic file
         *                          uploads for product images.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addProduct(
                        @Valid @RequestPart("product") ProductRequestDto productRequestDto,
                        MultipartHttpServletRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                productService.addProduct(productRequestDto, request)));
        }

        /**
         * Endpoint for a seller to update an existing product.
         *
         * @param id      The ID of the product to update.
         * @param request The multipart request containing updated product details and
         *                optional new files.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> updateProduct(
                        @PathVariable("id") Long id,
                        MultipartHttpServletRequest request) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                "Update product."));
        }

        /**
         * Endpoint to list all products.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link ProductResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<ProductResponseDto>>> listProducts() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                productService.listProducts()));
        }

        /**
         * Endpoint to retrieve detailed information for a single product by its ID.
         *
         * @param id The ID of the product.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         {@link ProductDetailsResponseDto}.
         */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ProductDetailsResponseDto>> getProductById(
                        @PathVariable(required = true) Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                productService.getProductById(id)));
        }
}