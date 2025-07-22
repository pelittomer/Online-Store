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
import com.online_store.backend.api.shipper.utils.ShipperUtilsService;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperService {
    private final ShipperRepository shipperRepository;
    private final UploadService uploadService;
    private final CommonUtilsService commonUtilsService;
    private final ShipperUtilsService shipperUtilsService;

    /**
     * Creates a new shipper record, handles file upload for the logo, and generates
     * a unique API key.
     *
     * @param shipperRequestDto DTO containing details for the new shipper.
     * @param file              The logo file for the shipper.
     * @return The ShipperResponseDto of the newly created shipper.
     * @throws DuplicateResourceException If a shipper with the same name already
     *                                    exists.
     */
    @Transactional
    public String createShipper(ShipperRequestDto shipperRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        if (shipperRepository.findByName(shipperRequestDto.getName()).isPresent()) {
            throw new DuplicateResourceException(
                    "Shipper with name '" + shipperRequestDto.getName() + "' already exists.");
        }

        Upload upload = uploadService.createFile(file);

        Shipper newShipper = shipperUtilsService.shipperRequestMapper(shipperRequestDto, upload);
        shipperRepository.save(newShipper);

        return "Shipper created succesfully.";
    }

    /**
     * Retrieves all shippers from the database.
     *
     * @return A list of ShipperResponseDto.
     */
    @Transactional(readOnly = true)
    public List<ShipperResponseDto> listShippers() {
        return shipperRepository.findAll().stream()
                .map(shipperUtilsService::shipperResponseMapper)
                .collect(Collectors.toList());
    }

}
