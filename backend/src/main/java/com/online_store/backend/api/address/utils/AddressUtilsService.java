package com.online_store.backend.api.address.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressUtilsService {
    //repositories
    private final AddressRepository addressRepository;

    public Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to access/delete a non-existent address with ID: {}", id);
                    return new EntityNotFoundException("Address with ID " + id + " not found.");
                });
    }
}
