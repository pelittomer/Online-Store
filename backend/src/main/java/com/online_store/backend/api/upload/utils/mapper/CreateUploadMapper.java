package com.online_store.backend.api.upload.utils.mapper;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUploadMapper {

    public Upload uploadMapper(MultipartFile file) throws IOException {
        return Upload.builder()
                .fileContent(file.getBytes())
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .build();
    }

}
