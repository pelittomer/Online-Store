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

@RestController
@RequestMapping("/api/return-request")
@RequiredArgsConstructor
public class ReturnRequestController {
    private final ReturnRequestService returnRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReturnRequest(@RequestBody ReturnRequestDto returnRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(returnRequestService.createReturnRequest(returnRequestDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateReturnRequestStatus(@PathVariable Long id,
            @RequestBody UpdateReturnRequestDto updateReturnRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(returnRequestService.updateReturnRequestStatus(id, updateReturnRequestDto)));
    }
}
