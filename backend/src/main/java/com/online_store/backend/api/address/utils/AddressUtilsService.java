package com.online_store.backend.api.address.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Address-related operations.
 * This component provides helper methods to interact with the Address entity,
 * ensuring consistent error handling and logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AddressUtilsService {
    // repositories
    private final AddressRepository addressRepository;

    /**
     * Finds an Address by its ID.
     * 
     * @param id the ID of the address to find
     * @return The address entity with the given ID.
     * @throws EntityNotFoundException if no address with the specified ID is found.
     * @see com.online_store.backend.api.address.service.AddressService#deleteAddress(Long)
     * @see com.online_store.backend.api.order.service.OrderService#createOrder(Long)
     */
    public Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to access/delete a non-existent address with ID: {}", id);
                    return new EntityNotFoundException("Address with ID " + id + " not found.");
                });
    }
}
