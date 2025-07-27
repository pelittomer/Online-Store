package com.online_store.backend.api.upload.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadUtilsService {
    private final UploadRepository uploadRepository;

    public Upload findUploadById(Long uploadId) {
        return uploadRepository.findById(uploadId)
                .orElseThrow(() -> {
                    log.warn("Upload with ID {} not found.", uploadId);
                    return new EntityNotFoundException("Upload not found!");
                });
    }
}
