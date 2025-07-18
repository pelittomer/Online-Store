package com.online_store.backend.api.upload.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.upload.repository.UploadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadRepository uploadRepository;

}
