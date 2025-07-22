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
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final UploadService uploadService;
    private final CommonUtilsService commonUtilsService;
    private final BrandUtilsService brandUtilsService;

    @Transactional
    public String addBrand(BrandRequestDto brandRequestDto, MultipartFile file) {
        if (brandRepository.findByName(brandRequestDto.getName()).isPresent()) {
            // error
        }
        commonUtilsService.checkImageFileType(file);

        Upload logo = uploadService.createFile(file);

        Brand newBrand = brandUtilsService.createBrandMapper(brandRequestDto, logo);
        brandRepository.save(newBrand);

        return "Brand created successfuly.";
    }

    @Transactional(readOnly = true)
    public List<BrandResponseDto> listBrands() {
        return brandRepository.findAll().stream()
                .map(brandUtilsService::brandResponseMapper)
                .collect(Collectors.toList());
    }

}
