package com.online_store.backend.api.returnRequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.returnRequest.dto.request.ReturnRequestDto;
import com.online_store.backend.api.returnRequest.dto.request.UpdateReturnRequestDto;
import com.online_store.backend.api.returnRequest.service.ReturnRequestService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing return requests.
 * This controller provides endpoints for customers to initiate a return request
 * and for sellers to update the status of an existing request.
 */
@RestController
@RequestMapping("/api/return-request")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnRequestService;

    /**
     * Endpoint for a customer to create a new return request.
     * The request body must contain the ID of the order item to be returned.
     *
     * @param returnRequestDto The DTO containing the details for the return
     *                         request.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReturnRequest(@RequestBody ReturnRequestDto returnRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(returnRequestService.createReturnRequest(returnRequestDto)));
    }

    /**
     * Endpoint for a seller to update the status of a specific return request.
     * This typically involves approving or rejecting the request.
     *
     * @param id                     The ID of the return request to update.
     * @param updateReturnRequestDto The DTO containing the new status for the
     *                               request.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         success message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateReturnRequestStatus(@PathVariable Long id,
            @RequestBody UpdateReturnRequestDto updateReturnRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(returnRequestService.updateReturnRequestStatus(id, updateReturnRequestDto)));
    }
}
