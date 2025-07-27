package com.online_store.backend.api.brand.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandUtilsService {
    // repositories
    private final BrandRepository brandRepository;
    // utils
    private final CommonUtilsService commonUtilsService;

    public void validateBrandCreation(BrandRequestDto dto, MultipartFile file) {
        if (brandRepository.findByName(dto.getName()).isPresent()) {
            log.warn("Brand creation failed. Brand with name '{}' already exists.", dto.getName());
            throw new DuplicateResourceException("Brand with this name already exists.");
        }
        commonUtilsService.checkImageFileType(file);
    }
}
