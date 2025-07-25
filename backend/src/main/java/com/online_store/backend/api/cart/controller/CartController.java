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

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addProductToCart(@RequestBody CartRequestDto cartRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.addProductToCart(cartRequestDto)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> removeProductFromCart(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.removeProductFromCart(id)));
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<String>> clearAllCartItems() {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.clearAllCartItems()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> listCartItems() {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.listCartItems()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateCartItem(@RequestBody UpdateCartRequestDto cartRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(cartService.updateCartItem(cartRequestDto)));
    }
}
