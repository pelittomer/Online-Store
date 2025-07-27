package com.online_store.backend.api.upload.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;
import com.online_store.backend.api.upload.utils.UploadUtilsService;
import com.online_store.backend.api.upload.utils.mapper.CreateUploadMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {
    // repositories
    private final UploadRepository uploadRepository;
    // mappers
    private final CreateUploadMapper createUploadMapper;
    // utils
    private final UploadUtilsService uploadUtilsService;

    @Transactional
    public Upload createFile(MultipartFile file) {
        log.info("Creating a new upload record for file: {}", file.getOriginalFilename());
        try {
            Upload upload = createUploadMapper.uploadMapper(file);
            return uploadRepository.save(upload);
        } catch (IOException e) {
            log.error("Failed to read file content for upload: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    @Transactional
    public Upload updateExistingUploadContent(Upload currentFile, MultipartFile file) {
        log.info("Updating content for existing upload record with ID: {}", currentFile.getId());
        try {
            Upload uploadItem = uploadRepository.findById(currentFile.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Upload not found!"));

            uploadItem.setFileContent(file.getBytes());
            return uploadRepository.save(uploadItem);
        } catch (Exception e) {
            log.error("Failed to read file content for update: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    @Transactional(readOnly = true)
    public Upload getUploadById(Long id) {
        log.debug("Fetching upload record with ID: {}", id);
        return uploadUtilsService.findUploadById(id);
    }

}
