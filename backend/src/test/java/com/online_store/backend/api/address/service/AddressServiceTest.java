package com.online_store.backend.api.address.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.address.repository.AddressRepository;
import com.online_store.backend.api.address.utils.AddressUtilsService;
import com.online_store.backend.api.address.utils.mapper.CreateAddressMapper;
import com.online_store.backend.api.address.utils.mapper.GetAddressMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressService Unit Tests")
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CommonUtilsService commonUtilsService;

    @Mock
    private AddressUtilsService addressUtilsService;

    @Mock
    private GetAddressMapper getAddressMapper;

    @Mock
    private CreateAddressMapper createAddressMapper;

    @InjectMocks
    private AddressService addressService;

    private User currentUser;
    private User anotherUser;
    private AddressRequestDto addressRequestDto;
    private Address address1;
    private Address address2;
    private AddressResponseDto addressResponseDto1;
    private AddressResponseDto addressResponseDto2;

    @BeforeEach
    void setUp() {
        currentUser = User.builder().id(1L).email("user@example.com").build();
        anotherUser = User.builder().id(2L).email("another@example.com").build();

        addressRequestDto = AddressRequestDto.builder()
                .city("Istanbul")
                .street("Main St")
                .build();

        address1 = Address.builder()
                .id(101L)
                .city("Ankara")
                .street("Second St")
                .user(currentUser)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        address2 = Address.builder()
                .id(102L)
                .city("Izmir")
                .street("Third St")
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .build();

        addressResponseDto1 = AddressResponseDto.builder()
                .id(address1.getId())
                .city(address1.getCity())
                .street(address1.getStreet())
                .build();

        addressResponseDto2 = AddressResponseDto.builder()
                .id(address2.getId())
                .city(address2.getCity())
                .street(address2.getStreet())
                .build();
    }

    @Nested
    @DisplayName("addAddress")
    class AddAddressTests {

        @Test
        @DisplayName("should successfully add a new address for the current user")
        void addAddress_ShouldSaveNewAddress_WhenCalled() {
            Address newAddress = Address.builder()
                    .city(addressRequestDto.getCity())
                    .user(currentUser)
                    .build();
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(createAddressMapper.addressMapper(eq(addressRequestDto), eq(currentUser))).thenReturn(newAddress);
            when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

            String result = addressService.addAddress(addressRequestDto);

            assertEquals("Address created successfully.", result);
            verify(commonUtilsService).getCurrentUser();
            verify(createAddressMapper).addressMapper(eq(addressRequestDto), eq(currentUser));
            verify(addressRepository).save(eq(newAddress));
        }
    }

    @Nested
    @DisplayName("listAddresses")
    class ListAddressesTests {
        @Test
        @DisplayName("should return a sorted list of addresses for the current user")
        void listAddresses_ShouldReturnSortedList_WhenAddressesExist() {
            List<Address> addresses = Arrays.asList(address1, address2);
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(addressRepository.findByUser(eq(currentUser))).thenReturn(addresses);
            when(getAddressMapper.addressMapper(eq(address1))).thenReturn(addressResponseDto1);
            when(getAddressMapper.addressMapper(eq(address2))).thenReturn(addressResponseDto2);

            List<AddressResponseDto> result = addressService.listAddresses();

            assertNotNull(result);
            assertEquals(addressResponseDto2.getId(), result.get(0).getId());
            assertEquals(addressResponseDto1.getId(), result.get(1).getId());
            verify(commonUtilsService).getCurrentUser();
            verify(addressRepository).findByUser(eq(currentUser));
            verify(getAddressMapper, times(2)).addressMapper(any(Address.class));
        }

        @Test
        @DisplayName("should return an empty list when no addresses exist for the user")
        void listAddresses_ShouldReturnEmptyList_WhenNoAddressesExist() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(addressRepository.findByUser(eq(currentUser))).thenReturn(Collections.emptyList());

            List<AddressResponseDto> result = addressService.listAddresses();

            assertNotNull(result);
            assertEquals(0, result.size());
            verify(commonUtilsService).getCurrentUser();
            verify(addressRepository).findByUser(eq(currentUser));
            verify(getAddressMapper, never()).addressMapper(any(Address.class));
        }
    }

    @Nested
    @DisplayName("deleteAddress")
    class DeleteAddressTests {

        @Test
        @DisplayName("should successfully delete an address belonging to the current user")
        void deleteAddress_ShouldDeleteAddress_WhenUserIsOwner() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(addressUtilsService.findAddressById(eq(address1.getId()))).thenReturn(address1);
            doNothing().when(addressRepository).deleteById(address1.getId());

            String result = addressService.deleteAddress(address1.getId());

            assertEquals("Address deleted successfully.", result);
            verify(commonUtilsService).getCurrentUser();
            verify(addressUtilsService).findAddressById(eq(address1.getId()));
            verify(addressRepository).deleteById(eq(address1.getId()));
        }

        @Test
        @DisplayName("should throw AccessDeniedException when user attempts to delete another user's address")
        void deleteAddress_ShouldThrowException_WhenUserIsNotOwner() {
            // 
            Address otherUserAddress = Address.builder()
                    .id(201L)
                    .city("Bursa")
                    .user(anotherUser)
                    .build();
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(addressUtilsService.findAddressById(eq(otherUserAddress.getId()))).thenReturn(otherUserAddress);

            assertThrows(AccessDeniedException.class,
                    () -> addressService.deleteAddress(otherUserAddress.getId()));

            verify(commonUtilsService).getCurrentUser();
            verify(addressUtilsService).findAddressById(eq(otherUserAddress.getId()));
            verify(addressRepository, never()).deleteById(any());
        }
    }
}
