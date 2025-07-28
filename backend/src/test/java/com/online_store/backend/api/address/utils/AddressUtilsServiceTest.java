package com.online_store.backend.api.address.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressUtilsService Unit Tests")
public class AddressUtilsServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressUtilsService addressUtilsService;

    private Address address;
    private final Long existingAddressId = 1L;
    private final Long nonExistingAddressId = 99L;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(existingAddressId)
                .city("Istanbul")
                .street("Main St")
                .build();
    }

    @Test
    @DisplayName("findAddressById should return the address when ID exists")
    void findAddressById_ShouldReturnAddress_WhenIdExists() {
        when(addressRepository.findById(eq(existingAddressId))).thenReturn(Optional.of(address));

        Address foundAddress = addressUtilsService.findAddressById(existingAddressId);

        assertNotNull(foundAddress);
        assertEquals(existingAddressId, foundAddress.getId());
        assertEquals(address.getCity(), foundAddress.getCity());
        verify(addressRepository).findById(existingAddressId);
    }

    @Test
    @DisplayName("findAddressById should throw EntityNotFoundException when ID does not exist")
    void findAddressById_ShouldThrowException_WhenIdDoesNotExist() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> addressUtilsService.findAddressById(nonExistingAddressId));

        assertEquals("Address with ID " + nonExistingAddressId + " not found.", exception.getMessage());
        verify(addressRepository).findById(eq(nonExistingAddressId));
    }
}
