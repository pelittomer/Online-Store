package com.online_store.backend.api.company.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.repository.CompanyRepository;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyUtilsServie Unit Tests")
public class CompanyUtilsServiceTest {
    @Mock
    private CommonUtilsService commonUtilsService;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyUtilsService companyUtilsService;

    private User currentUser;
    private User anotherUser;
    private Company company;
    private final Long companyId = 1L;
    private final Long nonExistentCompanyId = 2L;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .id(1L)
                .email("current.user@example.com")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        anotherUser = User.builder()
                .id(3L)
                .email("another.user@example.com")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        company = Company.builder()
                .id(companyId)
                .name("Test company")
                .user(currentUser)
                .build();
    }

    @Nested
    @DisplayName("getCurrentUserCompany")
    class GetCurrentUserCompanyTests {

        @Test
        @DisplayName("should return company when found for current user")
        void getCurrentUserCompany_ShouldReturnCompany_WhenFound() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(companyRepository.findByUser(eq(currentUser))).thenReturn(Optional.of(company));

            Company result = companyUtilsService.getCurrentUserCompany();

            assertNotNull(result);
            assertEquals(company.getId(), result.getId());
            assertEquals(company.getName(), result.getName());
            assertEquals(company.getUser().getEmail(), result.getUser().getEmail());
            verify(commonUtilsService).getCurrentUser();
            verify(companyRepository).findByUser(eq(currentUser));
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when no company found for current user")
        void getCurrentUserCompany_ShouldThrowException_WhenNotFound() {
            when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
            when(companyRepository.findByUser(eq(currentUser))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> companyUtilsService.getCurrentUserCompany());

            assertEquals("Company not found!", exception.getMessage());
            verify(commonUtilsService).getCurrentUser();
            verify(companyRepository).findByUser(eq(currentUser));
        }
    }

    @Nested
    @DisplayName("findCompanyById")
    class findCompanyById {
        @Test
        @DisplayName("should return company when found by ID")
        void findCompanyById_ShouldReturnCompany_WhenFound() {
            when(companyRepository.findById(eq(companyId))).thenReturn(Optional.of(company));

            Company result = companyUtilsService.findCompanyById(companyId);

            assertNotNull(result);
            assertEquals(company.getId(), result.getId());
            assertEquals(company.getName(), result.getName());
            verify(companyRepository).findById(eq(companyId));
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when company not found by ID")
        void findCompanyById_ShouldThrowException_WhenNotFound() {
            when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> companyUtilsService.findCompanyById(nonExistentCompanyId));

            assertEquals("Company with ID " + nonExistentCompanyId + " not found.", exception.getMessage());
            verify(companyRepository).findById(eq(nonExistentCompanyId));
        }
    }

    @Nested
    @DisplayName("findCompanyByUser")
    class FindCompanyByUserTests {
        @Test
        @DisplayName("should return company when found for a given user")
        void findCompanyByUser_ShouldReturnCompany_WhenFound() {
            when(companyRepository.findByUser(eq(currentUser))).thenReturn(Optional.of(company));

            Company result = companyUtilsService.findCompanyByUser(currentUser);

            assertNotNull(result);
            assertEquals(company.getId(), result.getId());
            assertEquals(company.getName(), result.getName());
            assertEquals(company.getUser().getEmail(), result.getUser().getEmail());
            verify(companyRepository).findByUser(eq(currentUser));
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when no company found for a given user")
        void findCompanyByUser_ShouldThrowException_WhenNotFound() {
            when(companyRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> companyUtilsService.findCompanyByUser(anotherUser));

            assertEquals("Company not found for the current user.", exception.getMessage());
            verify(companyRepository).findByUser(eq(anotherUser));
        }
    }
}
