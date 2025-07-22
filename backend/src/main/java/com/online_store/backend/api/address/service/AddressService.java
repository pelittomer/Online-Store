package com.online_store.backend.api.address.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;
import com.online_store.backend.api.address.utils.AddressUtilsService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CommonUtilsService commonUtilsService;
    private final AddressUtilsService addressUtilsService;

    /**
     * Adds a new address for the current authenticated user.
     *
     * @param addressRequestDto The DTO containing the address details.
     * @return The response DTO of the newly created address.
     */
    public String addAddress(AddressRequestDto addressRequestDto) {
        User currentUser = commonUtilsService.getCurrentUser();
        Address address = addressUtilsService.addressRequestMapper(addressRequestDto, currentUser);
        addressRepository.save(address);
        return "Address created successfully.";
    }

    /**
     * Retrieves all addresses belonging to the current authenticated user,
     * sorted by creation date in descending order.
     *
     * @return A list of address response DTOs.
     */
    @Transactional(readOnly = true)
    public List<AddressResponseDto> listAddresses() {
        User currentUser = commonUtilsService.getCurrentUser();
        List<Address> addresses = addressRepository.findByUser(currentUser);
        return addresses.stream()
                .sorted(Comparator.comparing(Address::getCreatedAt).reversed())
                .map(addressUtilsService::addressResponseMapper)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an address by its ID, ensuring the current authenticated user is the
     * owner.
     *
     * @param id The ID of the address to delete.
     * @throws EntityNotFoundException If the address with the given ID is not
     *                                 found.
     * @throws AccessDeniedException   If the current user is not authorized to
     *                                 delete this address.
     */
    @Transactional
    public String deleteAddress(Long id) {
        User currentUser = commonUtilsService.getCurrentUser();
        Address addresses = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address with ID " + id + " not found."));
        if (!addresses.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this address.");
        }

        addressRepository.deleteById(id);
        return "Address deleted successfully.";
    }

}
