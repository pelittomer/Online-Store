package com.online_store.backend.api.upload.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * REST controller for handling file uploads and retrievals.
 * This controller provides an endpoint to serve a file from the database
 * by its unique ID.
 */
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    /**
     * Endpoint to retrieve a file by its ID.
     * The file is served as a resource with appropriate headers for content type
     * and disposition.
     *
     * @param id The ID of the file to retrieve.
     * @return A {@link ResponseEntity} containing the file as a {@link Resource}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getUploadById(@PathVariable Long id) {
        Upload uploadFile = uploadService.getUploadById(id);
        Resource resource = new ByteArrayResource(uploadFile.getFileContent());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + uploadFile.getFileName() + "\"");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploadFile.getFileType()))
                .headers(headers)
                .body(resource);
    }

}
