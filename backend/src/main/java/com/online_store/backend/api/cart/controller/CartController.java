package com.online_store.backend.api.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.cart.dto.request.CartRequestDto;
import com.online_store.backend.api.cart.dto.request.UpdateCartRequestDto;
import com.online_store.backend.api.cart.dto.response.CartResponseDto;
import com.online_store.backend.api.cart.service.CartService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing the user's shopping cart.
 * Provides endpoints for adding, removing, updating, and viewing cart items.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
        private final CartService cartService;

        /**
         * Endpoint to add a product to the user's cart.
         * If the product already exists, the quantity is updated.
         *
         * @param cartRequestDto The DTO containing the product, stock, and quantity to
         *                       add.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addProductToCart(@RequestBody CartRequestDto cartRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                cartService.addProductToCart(cartRequestDto)));
        }

        /**
         * Endpoint to remove a specific item from the user's cart.
         *
         * @param id The ID of the cart item to remove.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @DeleteMapping("{id}")
        public ResponseEntity<ApiResponse<String>> removeProductFromCart(@PathVariable Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                cartService.removeProductFromCart(id)));
        }

        /**
         * Endpoint to clear all items from the user's cart.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @DeleteMapping("/all")
        public ResponseEntity<ApiResponse<String>> clearAllCartItems() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                cartService.clearAllCartItems()));
        }

        /**
         * Endpoint to get a detailed view of all items in the user's cart.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         {@link CartResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<CartResponseDto>> listCartItems() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                cartService.listCartItems()));
        }

        /**
         * Endpoint to update the quantity of a specific item in the user's cart.
         *
         * @param cartRequestDto The DTO containing the cart item ID and the new
         *                       quantity.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PutMapping
        public ResponseEntity<ApiResponse<String>> updateCartItem(@RequestBody UpdateCartRequestDto cartRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                cartService.updateCartItem(cartRequestDto)));
        }
}
