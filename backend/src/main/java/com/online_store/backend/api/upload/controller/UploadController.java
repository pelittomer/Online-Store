package com.online_store.backend.api.upload.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.upload.service.UploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @GetMapping("/{id}")
    public String getUploadById(@RequestParam String param) {
        // This function retrieves an image by its unique ID.
        return new String();
    }

}
