package com.online_store.backend.api.returnRequest.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.returnRequest.repository.ReturnRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnRequestService {
    private final ReturnRequestRepository returnRequestRepository;

}
