package com.online_store.backend.api.shipper.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.service.ShipperService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing shipper operations.
 * This controller provides endpoints for creating a new shipper and
 * for retrieving a list of all existing shippers.
 */
@RestController
@RequestMapping("/api/shipper")
@RequiredArgsConstructor
public class ShipperController {
    private final ShipperService shipperService;

    /**
     * Endpoint for creating a new shipper.
     * This endpoint requires shipper details and a logo file.
     *
     * @param shipperRequestDto The DTO containing the shipper's details.
     * @param file              The {@link MultipartFile} for the shipper's logo.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createShipper(
            @Valid @RequestPart("shipper") ShipperRequestDto shipperRequestDto,
            @RequestPart(value = "file", required = true) MultipartFile file) {
        return ResponseEntity.ok(
                ApiResponse.success(shipperService.createShipper(shipperRequestDto, file)));
    }

    /**
     * Endpoint to retrieve a list of all shippers.
     *
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         list of {@link ShipperResponseDto}.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShipperResponseDto>>> listShippers() {
        return ResponseEntity.ok(
                ApiResponse.success("", shipperService.listShippers()));
    }
}
