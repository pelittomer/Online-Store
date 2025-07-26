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
import com.online_store.backend.api.address.utils.mapper.CreateAddressMapper;
import com.online_store.backend.api.address.utils.mapper.GeAddressMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    // repositories
    private final AddressRepository addressRepository;
    // utils
    private final CommonUtilsService commonUtilsService;
    private final AddressUtilsService addressUtilsService;
    // mappers
    private final GeAddressMapper getAddressMapper;
    private final CreateAddressMapper createAddressMapper;

    @Transactional
    public String addAddress(AddressRequestDto addressRequestDto) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Adding new address for user: {}", currentUser.getEmail());

        Address address = createAddressMapper.addressMapper(addressRequestDto, currentUser);
        addressRepository.save(address);

        log.info("Address created successfully with ID: {} for user: {}", address.getId(), currentUser.getEmail());

        return "Address created successfully.";
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDto> listAddresses() {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Listing addresses for user: {}", currentUser.getEmail());
        return addressRepository.findByUser(currentUser)
                .stream()
                .sorted(Comparator.comparing(Address::getCreatedAt).reversed())
                .map(getAddressMapper::addressMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteAddress(Long id) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Attempting to delete address with ID: {} for user: {}", id, currentUser.getEmail());

        Address addressToDelete = addressUtilsService.findAddressById(id);

        if (!addressToDelete.getUser().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to delete an address belonging to another user. Address ID: {}",
                    currentUser.getEmail(), id);
            throw new AccessDeniedException("You are not authorized to delete this address.");
        }

        addressRepository.deleteById(id);
        log.info("Address with ID: {} deleted successfully by user: {}", id, currentUser.getEmail());

        return "Address deleted successfully.";
    }

}
