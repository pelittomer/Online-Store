package com.online_store.backend.api.shipper.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Shipper-related operations.
 * This component provides helper methods for generating unique API keys
 * and retrieving shipper entities with consistent error handling and logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ShipperUtilsService {
    // repositories
    private final ShipperRepository shipperRepository;

    /**
     * Generates a unique API key for a new shipper.
     * It creates a UUID, removes hyphens, and ensures the generated key
     * does not already exist in the database.
     *
     * @return A unique API key as a {@link String}.
     * @see com.online_store.backend.api.shipper.utils.mapper.CreateShipperMapper#shipperMapper(com.online_store.backend.api.shipper.dto.request.ShipperRequestDto,
     *      com.online_store.backend.api.upload.entities.Upload)
     */
    public String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        } while (shipperRepository.findByApiKey(apiKey).isPresent());
        return apiKey;
    }

    /**
     * Finds a shipper by their unique identifier.
     *
     * @param shipperId The ID of the shipper to be retrieved.
     * @return The {@link Shipper} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no shipper with the given ID exists.
     * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      org.springframework.web.multipart.MultipartHttpServletRequest)
     */
    public Shipper findShipperById(Long shipperId) {
        return shipperRepository.findById(shipperId)
                .orElseThrow(() -> {
                    log.warn("Shipper with ID {} not found.", shipperId);
                    return new EntityNotFoundException("Shipper not found!");
                });
    }
}
