package com.online_store.backend.api.company.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateStatusRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.entities.CompanyStatus;
import com.online_store.backend.api.company.repository.CompanyRepository;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.company.utils.mapper.CreateCompanyMapper;
import com.online_store.backend.api.company.utils.mapper.GetCompanyDetailsMapper;
import com.online_store.backend.api.company.utils.mapper.GetCompanyMapper;
import com.online_store.backend.api.company.utils.mapper.UpdateCompanyMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyService Unit Tests")
public class CompanyServiceTest {
        @Mock
        private CompanyRepository companyRepository;

        @Mock
        private CommonUtilsService commonUtilsService;

        @Mock
        private CompanyUtilsService companyUtilsService;

        @Mock
        private CreateCompanyMapper createCompanyMapper;

        @Mock
        private UpdateCompanyMapper updateCompanyMapper;

        @Mock
        private GetCompanyDetailsMapper getCompanyDetailsMapper;

        @Mock
        private GetCompanyMapper getCompanyMapper;

        @Mock
        private UploadService uploadService;

        @InjectMocks
        private CompanyService companyService;

        private User currentUser;
        private CompanyRequestDto companyRequestDto;
        private CompanyUpdateRequestDto companyUpdateRequestDto;
        private CompanyUpdateStatusRequestDto companyUpdateStatusRequestDto;
        private MultipartFile mockFile;
        private Upload mockUpload;
        private Company company;
        private CompanyPrivateResponseDto companyPrivateResponseDto;
        private CompanyResponseDto companyResponseDto;

        @BeforeEach
        void setUp() {
                currentUser = User.builder().id(1L).email("user@example.com").build();

                companyRequestDto = CompanyRequestDto.builder()
                                .name("New Company")
                                .taxId("TAX123")
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

                mockFile = new MockMultipartFile("logo", "logo.png", "image/png", "some-image-data".getBytes());
                mockUpload = Upload.builder().id(1L).build();

                company = Company.builder()
                                .id(1L)
                                .name("My Company")
                                .taxId("MYTAXID")
                                .user(currentUser)
                                .logo(mockUpload)
                                .status(CompanyStatus.PENDING)
                                .build();

                companyPrivateResponseDto = CompanyPrivateResponseDto.builder()
                                .id(1L)
                                .name("My Company")
                                .taxId("MYTAXID")
                                .status(CompanyStatus.PENDING)
                                .build();

                companyResponseDto = CompanyResponseDto.builder()
                                .id(1L)
                                .name("My Company")
                                .status(CompanyStatus.PENDING)
                                .build();
        }

        @Nested
        @DisplayName("createCompany")
        class CreateCompanyTests {

                @Test
                @DisplayName("should successfully create a new company")
                void createCompany_ShouldCreateCompany_WhenValidDataProvided() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        doNothing().when(commonUtilsService).checkImageFileType(eq(mockFile));
                        when(companyRepository.findByUser(any(User.class))).thenReturn(Optional.empty());
                        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());
                        when(companyRepository.findByTaxId(any(String.class))).thenReturn(Optional.empty());
                        when(uploadService.createFile(eq(mockFile))).thenReturn(mockUpload);
                        when(createCompanyMapper.companyMapper(eq(companyRequestDto), eq(mockUpload), eq(currentUser)))
                                        .thenReturn(company);
                        when(companyRepository.save(any(Company.class))).thenReturn(company);

                        String result = companyService.createCompany(companyRequestDto, mockFile);

                        assertEquals("Company created succesfully.", result);
                        verify(commonUtilsService).getCurrentUser();
                        verify(commonUtilsService).checkImageFileType(eq(mockFile));
                        verify(companyRepository).findByUser(eq(currentUser));
                        verify(companyRepository).findByName(eq(companyRequestDto.getName()));
                        verify(companyRepository).findByTaxId(eq(companyRequestDto.getTaxId()));
                        verify(uploadService).createFile(eq(mockFile));
                        verify(createCompanyMapper).companyMapper(eq(companyRequestDto), eq(mockUpload),
                                        eq(currentUser));
                        verify(companyRepository).save(eq(company));
                }

                @Test
                @DisplayName("should throw DuplicateResourceException when user already has a company")
                void createCompany_ShouldThrowException_WhenUserAlreadyHasCompany() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        doNothing().when(commonUtilsService).checkImageFileType(eq(mockFile));
                        when(companyRepository.findByUser(eq(currentUser))).thenReturn(Optional.of(company));

                        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                                        () -> companyService.createCompany(companyRequestDto, mockFile));

                        assertEquals("You can only create one company per user account.", exception.getMessage());
                        verify(commonUtilsService).getCurrentUser();
                        verify(commonUtilsService).checkImageFileType(eq(mockFile));
                        verify(companyRepository).findByUser(eq(currentUser));
                        verify(companyRepository, never()).findByName(any(String.class));
                        verify(companyRepository, never()).findByTaxId(any(String.class));
                        verify(uploadService, never()).createFile(any());
                        verify(createCompanyMapper, never()).companyMapper(any(), any(), any());
                        verify(companyRepository, never()).save(any());
                }

                @Test
                @DisplayName("should throw DuplicateResourceException when company name already exists")
                void createCompany_ShouldThrowException_WhenCompanyNameExists() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        doNothing().when(commonUtilsService).checkImageFileType(eq(mockFile));
                        when(companyRepository.findByUser(any(User.class))).thenReturn(Optional.empty());
                        when(companyRepository.findByName(eq(companyRequestDto.getName())))
                                        .thenReturn(Optional.of(company));

                        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                                        () -> companyService.createCompany(companyRequestDto, mockFile));

                        assertEquals("Company with name '" + companyRequestDto.getName() + "' already exists.",
                                        exception.getMessage());
                        verify(commonUtilsService).getCurrentUser();
                        verify(commonUtilsService).checkImageFileType(eq(mockFile));
                        verify(companyRepository).findByUser(eq(currentUser));
                        verify(companyRepository).findByName(eq(companyRequestDto.getName()));
                        verify(companyRepository, never()).findByTaxId(any(String.class));
                        verify(uploadService, never()).createFile(any());
                        verify(createCompanyMapper, never()).companyMapper(any(), any(), any());
                        verify(companyRepository, never()).save(any());
                }

                @Test
                @DisplayName("should throw DuplicateResourceException when tax ID already exists")
                void createCompany_ShouldThrowException_WhenTaxIdExists() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        doNothing().when(commonUtilsService).checkImageFileType(eq(mockFile));
                        when(companyRepository.findByUser(any(User.class))).thenReturn(Optional.empty());
                        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());
                        when(companyRepository.findByTaxId(eq(companyRequestDto.getTaxId())))
                                        .thenReturn(Optional.of(company));

                        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                                        () -> companyService.createCompany(companyRequestDto, mockFile));

                        assertEquals("Company with Tax ID '" + companyRequestDto.getTaxId() + "' already exists.",
                                        exception.getMessage());
                        verify(commonUtilsService).getCurrentUser();
                        verify(commonUtilsService).checkImageFileType(eq(mockFile));
                        verify(companyRepository).findByUser(eq(currentUser));
                        verify(companyRepository).findByName(eq(companyRequestDto.getName()));
                        verify(companyRepository).findByTaxId(eq(companyRequestDto.getTaxId()));
                        verify(uploadService, never()).createFile(any());
                        verify(createCompanyMapper, never()).companyMapper(any(), any(), any());
                        verify(companyRepository, never()).save(any());
                }
        }

        @Nested
        @DisplayName("updateMyCompany")
        class UpdateMyCompanyTests {

                @Test
                @DisplayName("should successfully update company details without changing logo")
                void updateMyCompany_ShouldUpdateDetails_WhenNoFileProvided() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        when(companyUtilsService.findCompanyByUser(eq(currentUser))).thenReturn(company);
                        when(updateCompanyMapper.companyMapper(eq(company), eq(companyUpdateRequestDto)))
                                        .thenReturn(company);
                        when(companyRepository.save(any(Company.class))).thenReturn(company);

                        String result = companyService.updateMyCompany(companyUpdateRequestDto, null);

                        assertEquals("Company updated succesfully.", result);
                        verify(commonUtilsService).getCurrentUser();
                        verify(companyUtilsService).findCompanyByUser(eq(currentUser));
                        verify(commonUtilsService, never()).checkImageFileType(any());
                        verify(uploadService, never()).updateExistingUploadContent(any(), any());
                        verify(updateCompanyMapper).companyMapper(eq(company), eq(companyUpdateRequestDto));
                        verify(companyRepository).save(eq(company));
                }

                @Test
                @DisplayName("should successfully update company details and logo")
                void updateMyCompany_ShouldUpdateDetailsAndLogo_WhenFileProvided() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        when(companyUtilsService.findCompanyByUser(eq(currentUser))).thenReturn(company);
                        doNothing().when(commonUtilsService).checkImageFileType(eq(mockFile));
                        when(uploadService.updateExistingUploadContent(eq(mockUpload), eq(mockFile)))
                                        .thenReturn(mockUpload); 
                        when(updateCompanyMapper.companyMapper(eq(company), eq(companyUpdateRequestDto)))
                                        .thenReturn(company); 
                        when(companyRepository.save(any(Company.class))).thenReturn(company);

                        String result = companyService.updateMyCompany(companyUpdateRequestDto, mockFile);

                        assertEquals("Company updated succesfully.", result);
                        verify(commonUtilsService).getCurrentUser();
                        verify(companyUtilsService).findCompanyByUser(eq(currentUser));
                        verify(commonUtilsService).checkImageFileType(eq(mockFile));
                        verify(uploadService).updateExistingUploadContent(eq(mockUpload), eq(mockFile));
                        verify(updateCompanyMapper).companyMapper(eq(company), eq(companyUpdateRequestDto));
                        verify(companyRepository).save(eq(company));
                }
        }

        @Nested
        @DisplayName("getMyCompany")
        class GetMyCompanyTests {

                @Test
                @DisplayName("should return private company details for the current user")
                void getMyCompany_ShouldReturnPrivateDetails_WhenCompanyExists() {
                        when(commonUtilsService.getCurrentUser()).thenReturn(currentUser);
                        when(companyUtilsService.findCompanyByUser(eq(currentUser))).thenReturn(company);
                        when(getCompanyDetailsMapper.companyMapper(eq(company))).thenReturn(companyPrivateResponseDto);

                        CompanyPrivateResponseDto result = companyService.getMyCompany();

                        assertNotNull(result);
                        assertEquals(companyPrivateResponseDto.getId(), result.getId());
                        assertEquals(companyPrivateResponseDto.getName(), result.getName());
                        assertEquals(companyPrivateResponseDto.getStatus(), result.getStatus());
                        verify(commonUtilsService).getCurrentUser();
                        verify(companyUtilsService).findCompanyByUser(eq(currentUser));
                        verify(getCompanyDetailsMapper).companyMapper(eq(company));
                }
        }

        @Nested
        @DisplayName("updateCompanyStatus")
        class UpdateCompanyStatusTests {

                @Test
                @DisplayName("should successfully update company status")
                void updateCompanyStatus_ShouldUpdateStatus_WhenCalled() {
                        Long companyId = 1L;
                        Company existingCompany = Company.builder().id(companyId).status(CompanyStatus.PENDING).build();
                        when(companyUtilsService.findCompanyById(eq(companyId))).thenReturn(existingCompany);
                        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

                        String result = companyService.updateCompanyStatus(companyId, companyUpdateStatusRequestDto);

                        assertEquals("Company status updated succesfully.", result);
                        assertEquals(CompanyStatus.APPROVED, existingCompany.getStatus());
                        verify(companyUtilsService).findCompanyById(eq(companyId));
                        verify(companyRepository).save(eq(existingCompany));
                }
        }

        @Nested
        @DisplayName("getCompanyById")
        class GetCompanyByIdTests {

                @Test
                @DisplayName("should return public company details by ID")
                void getCompanyById_ShouldReturnPublicDetails_WhenCompanyExists() {
                        Long companyId = 1L;
                        when(companyUtilsService.findCompanyById(eq(companyId))).thenReturn(company);
                        when(getCompanyMapper.companyMapper(eq(company))).thenReturn(companyResponseDto);

                        CompanyResponseDto result = companyService.getCompanyById(companyId);

                        assertNotNull(result);
                        assertEquals(companyResponseDto.getId(), result.getId());
                        assertEquals(companyResponseDto.getName(), result.getName());
                        verify(companyUtilsService).findCompanyById(eq(companyId));
                        verify(getCompanyMapper).companyMapper(eq(company));
                }
        }

        @Nested
        @DisplayName("listAllCompanies")
        class ListAllCompaniesTests {

                @Test
                @DisplayName("should return a list of all companies")
                void listAllCompanies_ShouldReturnAllCompanies_WhenCalled() {
                        List<Company> allCompanies = Arrays.asList(company);
                        List<CompanyResponseDto> allCompanyResponseDtos = Arrays.asList(companyResponseDto);
                        when(companyRepository.findAll()).thenReturn(allCompanies);
                        when(getCompanyMapper.companyMapper(eq(company))).thenReturn(companyResponseDto);

                        List<CompanyResponseDto> result = companyService.listAllCompanies();

                        assertNotNull(result);
                        assertEquals(1, result.size());
                        assertEquals(allCompanyResponseDtos.get(0).getName(), result.get(0).getName());
                        verify(companyRepository).findAll();
                        verify(getCompanyMapper).companyMapper(eq(company));
                }

                @Test
                @DisplayName("should return an empty list when no companies exist")
                void listAllCompanies_ShouldReturnEmptyList_WhenNoCompaniesExist() {
                        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

                        List<CompanyResponseDto> result = companyService.listAllCompanies();

                        assertNotNull(result);
                        assertEquals(0, result.size());
                        verify(companyRepository).findAll();
                        verify(getCompanyMapper, never()).companyMapper(any(Company.class));
                }
        }

}
