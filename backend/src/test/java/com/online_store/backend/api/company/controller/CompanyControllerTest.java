package com.online_store.backend.api.company.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateStatusRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entities.CompanyStatus;
import com.online_store.backend.api.company.service.CompanyService;
import com.online_store.backend.common.exception.ApiResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyController Unit Tests")
public class CompanyControllerTest {
    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private CompanyRequestDto companyRequestDto;
    private CompanyUpdateRequestDto companyUpdateRequestDto;
    private CompanyUpdateStatusRequestDto companyUpdateStatusRequestDto;
    private MultipartFile mockFile;
    private CompanyPrivateResponseDto companyPrivateResponseDto;
    private CompanyResponseDto companyResponseDto;
    private List<CompanyResponseDto> companyResponseDtoList;
    private final Long companyId = 1L;

    @BeforeEach
    void setUp() {
        companyRequestDto = CompanyRequestDto.builder()
                .name("Test Company")
                .taxId("TAX12345")
                .phone("1234567890")
                .build();

        companyUpdateRequestDto = CompanyUpdateRequestDto.builder()
                .name("Updated Company Name")
                .phone("0987654321")
                .build();

        companyUpdateStatusRequestDto = CompanyUpdateStatusRequestDto.builder()
                .status(CompanyStatus.APPROVED)
                .rejectionReason(null)
                .build();

        mockFile = new MockMultipartFile("file", "test.png", "image/png", "some image content".getBytes());

        companyPrivateResponseDto = CompanyPrivateResponseDto.builder()
                .id(companyId)
                .name("My Company")
                .taxId("MYTAXID")
                .status(CompanyStatus.PENDING)
                .build();

        companyResponseDto = CompanyResponseDto.builder()
                .id(companyId)
                .name("Public Company")
                .status(CompanyStatus.APPROVED)
                .build();

        companyResponseDtoList = Collections.singletonList(companyResponseDto);
    }

    @Nested
    @DisplayName("createCompany")
    class CreateCompanyTests {
        @Test
        @DisplayName("should return 200 OK and success message")
        void createCompany_ShouldReturnOkAndSuccessMessage() {
            String successMessage = "Company created succesfully.";
            when(companyService.createCompany(eq(companyRequestDto), eq(mockFile))).thenReturn(successMessage);

            ResponseEntity<ApiResponse<String>> response = companyController.createCompany(companyRequestDto, mockFile);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(successMessage, response.getBody().getData());
            verify(companyService).createCompany(eq(companyRequestDto), eq(mockFile));
        }
    }

    @Nested
    @DisplayName("updateMyCompany")
    class UpdateMyCompanyTests {
        @Test
        @DisplayName("should return 200 OK and success message when file is provided")
        void updateMyCompany_ShouldReturnOkAndSuccessMessage_WhenFileProvided() {
            String successMessage = "Company updated succesfully.";
            when(companyService.updateMyCompany(eq(companyUpdateRequestDto), eq(mockFile))).thenReturn(successMessage);

            ResponseEntity<ApiResponse<String>> response = companyController.updateMyCompany(companyUpdateRequestDto,
                    mockFile);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(successMessage, response.getBody().getData());
            verify(companyService).updateMyCompany(eq(companyUpdateRequestDto), eq(mockFile));
        }

        @Test
        @DisplayName("should return 200 OK and success message when file is null")
        void updateMyCompany_ShouldReturnOkAndSuccessMessage_WhenFileIsNull() {
            String successMessage = "Company updated succesfully.";
            when(companyService.updateMyCompany(eq(companyUpdateRequestDto), eq(null))).thenReturn(successMessage);

            ResponseEntity<ApiResponse<String>> response = companyController.updateMyCompany(companyUpdateRequestDto,
                    null);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(successMessage, response.getBody().getData());
            verify(companyService).updateMyCompany(eq(companyUpdateRequestDto), eq(null));
        }
    }

    @Nested
    @DisplayName("getMyCompany")
    class GetMyCompanyTests {
        @Test
        @DisplayName("should return 200 OK and private company details")
        void getMyCompany_ShouldReturnOkAndPrivateCompanyDetails() {
            when(companyService.getMyCompany()).thenReturn(companyPrivateResponseDto);

            ResponseEntity<ApiResponse<CompanyPrivateResponseDto>> response = companyController.getMyCompany();

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(companyPrivateResponseDto, response.getBody().getData());
            verify(companyService).getMyCompany();
        }
    }

    @Nested
    @DisplayName("updateCompanyStatus")
    class UpdateCompanyStatusTests {
        @Test
        @DisplayName("should return 200 OK and success message")
        void updateCompanyStatus_ShouldReturnOkAndSuccessMessage() {
            String successMessage = "Company status updated succesfully.";
            when(companyService.updateCompanyStatus(eq(companyId), eq(companyUpdateStatusRequestDto)))
                    .thenReturn(successMessage);

            ResponseEntity<ApiResponse<String>> response = companyController.updateCompanyStatus(companyId,
                    companyUpdateStatusRequestDto);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(successMessage, response.getBody().getData());
            verify(companyService).updateCompanyStatus(eq(companyId), eq(companyUpdateStatusRequestDto));
        }
    }

    @Nested
    @DisplayName("getCompanyById")
    class GetCompanyByIdTests {
        @Test
        @DisplayName("should return 200 OK and public company details")
        void getCompanyById_ShouldReturnOkAndPublicCompanyDetails() {
            when(companyService.getCompanyById(eq(companyId))).thenReturn(companyResponseDto);

            ResponseEntity<ApiResponse<CompanyResponseDto>> response = companyController.getCompanyById(companyId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(companyResponseDto, response.getBody().getData());
            verify(companyService).getCompanyById(eq(companyId));
        }
    }

    @Nested
    @DisplayName("listAllCompanies")
    class ListAllCompaniesTests {
        @Test
        @DisplayName("should return 200 OK and list of all companies")
        void listAllCompanies_ShouldReturnOkAndListOfAllCompanies() {
            when(companyService.listAllCompanies()).thenReturn(companyResponseDtoList);

            ResponseEntity<ApiResponse<List<CompanyResponseDto>>> response = companyController.listAllCompanies();

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(companyResponseDtoList, response.getBody().getData());
            verify(companyService).listAllCompanies();
        }

        @Test
        @DisplayName("should return 200 OK and empty list when no companies exist")
        void listAllCompanies_ShouldReturnOkAndEmptyList_WhenNoCompaniesExist() {
            when(companyService.listAllCompanies()).thenReturn(Collections.emptyList());

            ResponseEntity<ApiResponse<List<CompanyResponseDto>>> response = companyController.listAllCompanies();

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("", response.getBody().getMessage());
            assertEquals(Collections.emptyList(), response.getBody().getData());
            verify(companyService).listAllCompanies();
        }
    }
}
