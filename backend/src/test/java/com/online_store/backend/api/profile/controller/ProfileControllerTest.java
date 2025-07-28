package com.online_store.backend.api.profile.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Gender;
import com.online_store.backend.api.profile.service.ProfileService;
import com.online_store.backend.common.exception.ApiResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileController Unit Tests")
public class ProfileControllerTest {
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ProfileResponseDto profileResponseDto;
    private ProfileRequestDto profileRequestDto;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        profileResponseDto = ProfileResponseDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.PREFER_NOT_TO_SAY)
                .build();

        profileRequestDto = ProfileRequestDto.builder()
                .firstName("Jane")
                .gender(Gender.FEMALE)
                .build();

        file = new MockMultipartFile(
                "file",
                "avatar.jpg",
                "image/jpeg",
                "test-data".getBytes());
    }

    @Test
    @DisplayName("getProfileDetails should return current user's profile details with 200 OK")
    void getProfileDetails_ShouldReturnProfileDetails_WhenCalled() {
        when(profileService.getProfileDetails()).thenReturn(profileResponseDto);

        ResponseEntity<ApiResponse<ProfileResponseDto>> responseEntity = profileController.getProfileDetails();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse<ProfileResponseDto> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(profileResponseDto, apiResponse.getData());
        verify(profileService).getProfileDetails();
    }

    @Test
    @DisplayName("updateProfileDetails with file should return a success message with 200 OK")
    void updateProfileDetails_WithFile_ShouldReturnSuccessMessage() {
        String successMessage = "Profile updated successfully for user."; 

        when(profileService.updateProfileDetails(eq(profileRequestDto), eq(file)))
                .thenReturn(successMessage);

        ResponseEntity<ApiResponse<String>> responseEntity = profileController.updateProfileDetails(profileRequestDto,
                file);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<String> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(successMessage, apiResponse.getData());

        verify(profileService).updateProfileDetails(eq(profileRequestDto), eq(file));
    }

    @Test
    @DisplayName("updateProfileDetails without file should return a success message with 200 OK")
    void updateProfileDetails_WithoutFile_ShouldReturnSuccessMessage() {
        String successMessage = "Profile updated successfully for user.";
        when(profileService.updateProfileDetails(eq(profileRequestDto), eq(null)))
                .thenReturn(successMessage);

        ResponseEntity<ApiResponse<String>> responseEntity = profileController.updateProfileDetails(profileRequestDto,
                null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<String> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(successMessage, apiResponse.getData());
        verify(profileService).updateProfileDetails(eq(profileRequestDto), eq(null));
    }
}
