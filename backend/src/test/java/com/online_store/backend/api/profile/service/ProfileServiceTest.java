package com.online_store.backend.api.profile.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Gender;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.profile.repository.ProfileRepository;
import com.online_store.backend.api.profile.utils.mapper.GetProfileMapper;
import com.online_store.backend.api.profile.utils.mapper.UpdateProfileMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileService Unit Tests")
public class ProfileServiceTest {
    @Mock
    private CommonUtilsService commonUtilsService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private GetProfileMapper getProfileMapper;

    @Mock
    private UpdateProfileMapper updateProfileMapper;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private ProfileService profileService;

    private User currentUser;
    private Profile currentProfile;
    private ProfileResponseDto profileResponseDto;
    private ProfileRequestDto profileRequestDto;
    private MultipartFile avatarFile;
    private Upload existingAvatar;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.CUSTOMER)
                .build();

        existingAvatar = Upload.builder()
                .id(1L)
                .build();

        currentProfile = Profile.builder()
                .id(1L)
                .user(currentUser)
                .gender(Gender.PREFER_NOT_TO_SAY)
                .avatar(existingAvatar)
                .build();

        profileResponseDto = ProfileResponseDto.builder()
                .id(1L)
                .gender(Gender.PREFER_NOT_TO_SAY)
                .build();

        avatarFile = new MockMultipartFile(
                "avatar",
                "avatar.jpg",
                "image/jpeg",
                "test image content".getBytes());
    }

    @Nested
    @DisplayName("getProfileDetails")
    class GetProfileDetailsTests {
        @Test
        @DisplayName("should return profile details for the authenticated user")
        void getProfileDetails_ShouldReturnProfileDetails_WhenUserIsAuthenticated() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(profileRepository.findByUser(eq(currentUser))).thenReturn(currentProfile);
            when(getProfileMapper.profileMapper(eq(currentProfile))).thenReturn(profileResponseDto);

            ProfileResponseDto result = profileService.getProfileDetails();

            assertNotNull(result);
            assertEquals(profileResponseDto.getGender(), result.getGender());
            verify(commonUtilsService).getCurrentUser();
            verify(profileRepository).findByUser(eq(currentUser));
            verify(getProfileMapper).profileMapper(eq(currentProfile));
        }
    }

    @Nested
    @DisplayName("updateProfileDetails")
    class UpdateProfileDetailsTests {
        @Test
        @DisplayName("should update profile details without changing avatar")

        void updateProfileDetails_ShouldUpdateDetails_WhenNoFileIsProvided() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(profileRepository.findByUser(eq(currentUser))).thenReturn(currentProfile);

            when(updateProfileMapper.profileMapper(eq(currentProfile), eq(profileRequestDto)))
                    .thenReturn(currentProfile);

            when(profileRepository.save(any(Profile.class))).thenReturn(currentProfile);

            String result = profileService.updateProfileDetails(profileRequestDto, null);

            assertEquals("Profile updated successfully for user.", result);
            verify(commonUtilsService).getCurrentUser();
            verify(profileRepository).findByUser(eq(currentUser));
            verify(updateProfileMapper).profileMapper(eq(currentProfile), eq(profileRequestDto));
            verify(profileRepository).save(eq(currentProfile));
            verify(uploadService, never()).updateExistingUploadContent(any(), any());
            verify(uploadService, never()).createFile(any());
        }

        @Test
        @DisplayName("should update profile details and update existing avatar")
        void updateProfileDetails_ShouldUpdateAvatar_WhenFileIsProvidedAndAvatarExists() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(profileRepository.findByUser(eq(currentUser))).thenReturn(currentProfile);
            doNothing().when(commonUtilsService).checkImageFileType(eq(avatarFile));

            when(uploadService.updateExistingUploadContent(eq(existingAvatar), eq(avatarFile)))
                    .thenReturn(existingAvatar);

            when(updateProfileMapper.profileMapper(eq(currentProfile), eq(profileRequestDto)))
                    .thenReturn(currentProfile);

            when(profileRepository.save(any(Profile.class))).thenReturn(currentProfile);

            String result = profileService.updateProfileDetails(profileRequestDto, avatarFile);

            assertEquals("Profile updated successfully for user.", result);
            verify(commonUtilsService).getCurrentUser();
            verify(profileRepository).findByUser(eq(currentUser));
            verify(commonUtilsService).checkImageFileType(eq(avatarFile));
            verify(uploadService).updateExistingUploadContent(eq(existingAvatar), eq(avatarFile));
            verify(uploadService, never()).createFile(any());
            verify(updateProfileMapper).profileMapper(eq(currentProfile), eq(profileRequestDto));
            verify(profileRepository).save(eq(currentProfile));
        }

        @Test
        @DisplayName("should update profile details and create a new avatar")
        void updateProfileDetails_ShouldCreateNewAvatar_WhenFileIsProvidedAndNoAvatarExists() {
            currentProfile.setAvatar(null);
            Upload newAvatar = Upload.builder().id(2L).build();
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(profileRepository.findByUser(eq(currentUser))).thenReturn(currentProfile);
            doNothing().when(commonUtilsService).checkImageFileType(eq(avatarFile));
            when(uploadService.createFile(eq(avatarFile))).thenReturn(newAvatar);
            when(updateProfileMapper.profileMapper(eq(currentProfile), eq(profileRequestDto)))
                    .thenReturn(currentProfile);
            when(profileRepository.save(any(Profile.class))).thenReturn(currentProfile);

            String result = profileService.updateProfileDetails(profileRequestDto, avatarFile);

            assertEquals("Profile updated successfully for user.", result);
            assertEquals(newAvatar, currentProfile.getAvatar());
            verify(commonUtilsService).getCurrentUser();
            verify(profileRepository).findByUser(eq(currentUser));
            verify(commonUtilsService).checkImageFileType(eq(avatarFile));
            verify(uploadService, never()).updateExistingUploadContent(any(), any());
            verify(uploadService).createFile(eq(avatarFile));
            verify(updateProfileMapper).profileMapper(eq(currentProfile), eq(profileRequestDto));
            verify(profileRepository).save(eq(currentProfile));
        }
    }

    @Nested
    @DisplayName("createdProfile")
    class CreateProfileTests {

        @Test
        @DisplayName("should create a default profile for a customer")
        void createProfile_ShouldCreateDefaultProfile_WhenRoleIsCustomer() {
            Profile newProfile = profileService.createProfile(Role.CUSTOMER);

            assertNotNull(newProfile);
            assertEquals(Gender.PREFER_NOT_TO_SAY, newProfile.getGender());
            assertNull(newProfile.getAvatar());
            assertNull(newProfile.getUser());
        }

        @Test
        @DisplayName("should return null for an unsupported role")
        void createProfile_ShouldReturnNull_WhenRoleIsUnsupported() {
            Profile newProfile = profileService.createProfile(Role.ADMIN);

            assertNull(newProfile);
        }

    }
}
