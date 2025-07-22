package com.online_store.backend.api.address.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.service.AddressService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addAddress(@RequestBody AddressRequestDto addressRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(addressService.addAddress(addressRequestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> listAddresses() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        addressService.listAddresses()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(addressService.deleteAddress(id)));
    }
}
