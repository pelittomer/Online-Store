package com.online_store.backend.api.shipper.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;
import com.online_store.backend.api.shipper.utils.mapper.CreateShipperMapper;
import com.online_store.backend.api.shipper.utils.mapper.GetShipperMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipperService {
    // repositories
    private final ShipperRepository shipperRepository;
    // services
    private final UploadService uploadService;
    // utils
    private final CommonUtilsService commonUtilsService;
    // mappers
    private final GetShipperMapper getShipperMapper;
    private final CreateShipperMapper createShipperMapper;

    @Transactional
    public String createShipper(ShipperRequestDto shipperRequestDto, MultipartFile file) {
        log.info("Attempting to create a new shipper with name: {}", shipperRequestDto.getName());
        commonUtilsService.checkImageFileType(file);
        
        shipperRepository.findByName(shipperRequestDto.getName()).ifPresent(shipper -> {
            log.warn("Shipper with name '{}' already exists. Creation aborted.", shipperRequestDto.getName());
            throw new DuplicateResourceException(
                    "Shipper with name '" + shipperRequestDto.getName() + "' already exists.");
        });

        Upload upload = uploadService.createFile(file);

        Shipper newShipper = createShipperMapper.shipperMapper(shipperRequestDto, upload);
        shipperRepository.save(newShipper);

        log.info("Shipper '{}' created successfully with ID: {}", newShipper.getName(), newShipper.getId());
        return "Shipper created succesfully.";
    }

    @Transactional(readOnly = true)
    public List<ShipperResponseDto> listShippers() {
        log.info("Listing all shippers.");
        return shipperRepository.findAll().stream()
                .map(getShipperMapper::shipperMapper)
                .collect(Collectors.toList());
    }

}
