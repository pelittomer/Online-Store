package com.online_store.backend.api.address.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.service.AddressService;
import com.online_store.backend.common.exception.ApiResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressController Unit Tests")
public class AddressControllerTest {
    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private AddressRequestDto addressRequestDto;
    private AddressResponseDto addressResponseDto;
    private List<AddressResponseDto> addressResponseDtoList;
    private final Long addressId = 1L;

    @BeforeEach
    void setUp() {
        addressRequestDto = AddressRequestDto.builder()
                .city("Istanbul")
                .build();

        addressResponseDto = AddressResponseDto.builder()
                .id(addressId)
                .city("Istanbul")
                .build();

        addressResponseDtoList = Collections.singletonList(addressResponseDto);
    }

    @Test
    @DisplayName("addAddress should return a success message with 200 OK")
    void addAddress_ShouldReturnSuccessMessage_WhenCalled() {
        String successMessage = "Address created successfully.";
        when(addressService.addAddress(addressRequestDto)).thenReturn(successMessage);

        ResponseEntity<ApiResponse<String>> responseEntity = addressController.addAddress(addressRequestDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<String> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(successMessage, apiResponse.getData());
        verify(addressService).addAddress(eq(addressRequestDto));
    }

    @Test
    @DisplayName("listAddresses should return a list of addresses with 200 OK")
    void listAddresses_ShouldReturnListOfAddresses_WhenCalled() {
        when(addressService.listAddresses()).thenReturn(addressResponseDtoList);

        ResponseEntity<ApiResponse<List<AddressResponseDto>>> responseEntity = addressController.listAddresses();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<List<AddressResponseDto>> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(addressResponseDtoList, apiResponse.getData());
        verify(addressService).listAddresses();
    }

    @Test
    @DisplayName("deleteAddress should return a success message with 200 OK")
    void deleteAddress_ShouldReturnSuccessMessage_WhenCalled() {
        String successMessage = "Address deleted successfully.";
        when(addressService.deleteAddress(eq(addressId))).thenReturn(successMessage);

        ResponseEntity<ApiResponse<String>> responseEntity = addressController.deleteAddress(addressId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<String> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(successMessage, apiResponse.getData());
        verify(addressService).deleteAddress(eq(addressId));
    }

}
