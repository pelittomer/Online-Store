package com.online_store.backend.api.brand.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.api.brand.utils.BrandUtilsService;
import com.online_store.backend.api.brand.utils.mapper.CreteBrandMapper;
import com.online_store.backend.api.brand.utils.mapper.GetBrandMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
    // repositories
    private final BrandRepository brandRepository;
    // services
    private final UploadService uploadService;
    // utils
    private final BrandUtilsService brandUtilsService;
    // mappers
    private final CreteBrandMapper createBrandMapper;
    private final GetBrandMapper getBrandMapper;

    @Transactional
    public String addBrand(BrandRequestDto dto, MultipartFile file) {
        log.info("Attempting to add a new brand with name: '{}'", dto.getName());
        brandUtilsService.validateBrandCreation(dto, file);

        Upload logo = uploadService.createFile(file);
        Brand newBrand = createBrandMapper.brandMapper(dto, logo);
        brandRepository.save(newBrand);

        log.info("Brand '{}' created successfully with ID: {}.", newBrand.getName(), newBrand.getId());
        return "Brand created successfuly.";
    }

    @Transactional(readOnly = true)
    public List<BrandResponseDto> listBrands() {
        return brandRepository.findAll().stream()
                .map(getBrandMapper::brandMapper)
                .collect(Collectors.toList());
    }

}
