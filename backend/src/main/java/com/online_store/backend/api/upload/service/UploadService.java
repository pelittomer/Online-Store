package com.online_store.backend.api.upload.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadRepository uploadRepository;

    public Upload createFile(MultipartFile file) {
        try {
            Upload upload = Upload.builder()
                    .fileContent(file.getBytes())
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .build();
            return uploadRepository.save(upload);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    public Upload updateExistingUploadContent(Upload currentFile, MultipartFile file) {
        try {
            Upload uploadItem = uploadRepository.findById(currentFile.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Upload not found!"));

            uploadItem.setFileContent(file.getBytes());
            return uploadRepository.save(uploadItem);
        } catch (Exception e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    public Upload getUploadById(Long id) {
        return uploadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Upload not found!"));
    }

}
