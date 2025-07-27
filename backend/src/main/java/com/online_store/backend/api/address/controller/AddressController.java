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

/**
 * REST controller for managing user addresses.
 * Provides endpoints for adding, listing, and deleting addresses.
 */
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
        private final AddressService addressService;

        /**
         * Endpoint to add a new address for the authenticated user.
         *
         * @param addressRequestDto The DTO containing the address details.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addAddress(@RequestBody AddressRequestDto addressRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                addressService.addAddress(addressRequestDto)));
        }

        /**
         * Endpoint to retrieve all addresses belonging to the authenticated user.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link AddressResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<AddressResponseDto>>> listAddresses() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                addressService.listAddresses()));
        }

        /**
         * Endpoint to delete a specific address for the authenticated user.
         *
         * @param id The ID of the address to delete.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                addressService.deleteAddress(id)));
        }
}
