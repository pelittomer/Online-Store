package com.online_store.backend.api.upload.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Upload-related operations.
 * This component provides helper methods for retrieving upload entities
 * with consistent error handling and logging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadUtilsService {
    private final UploadRepository uploadRepository;

    /**
     * Finds an upload entity by its unique identifier.
     *
     * @param uploadId The ID of the upload to be retrieved.
     * @return The {@link Upload} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no upload with the given ID exists.
     * @see com.online_store.backend.api.upload.service.UploadService#getUploadById(Long)
     */
    public Upload findUploadById(Long uploadId) {
        return uploadRepository.findById(uploadId)
                .orElseThrow(() -> {
                    log.warn("Upload with ID {} not found.", uploadId);
                    return new EntityNotFoundException("Upload not found!");
                });
    }
}
