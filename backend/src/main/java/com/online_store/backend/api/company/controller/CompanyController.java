package com.online_store.backend.api.company.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateStatusRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.service.CompanyService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing company information.
 * This controller handles company creation, updates, and retrieval for both
 * authenticated users (sellers) and general public access.
 */
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
        private final CompanyService companyService;

        /**
         * Endpoint for a seller to create their company profile.
         * Requires company details and a logo file.
         *
         * @param companyRequestDto The DTO containing company details.
         * @param file              The company logo file.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> createCompany(
                        @Valid @RequestPart("company") CompanyRequestDto companyRequestDto,
                        @RequestPart("file") MultipartFile file) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.createCompany(companyRequestDto, file)));
        }

        /**
         * Endpoint for a seller to update their company details and/or logo.
         *
         * @param companyUpdateRequestDto The DTO containing the updated company
         *                                information.
         * @param file                    An optional new logo file.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PutMapping
        public ResponseEntity<ApiResponse<String>> updateMyCompany(
                        @Valid @RequestPart("company") CompanyUpdateRequestDto companyUpdateRequestDto,
                        @RequestPart(value = "file", required = false) MultipartFile file) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.updateMyCompany(companyUpdateRequestDto, file)));
        }

        /**
         * Endpoint for a seller to get their own company's detailed information.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         company's private details.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<CompanyPrivateResponseDto>> getMyCompany() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.getMyCompany()));
        }

        /**
         * Endpoint for an admin to update a company's status (e.g., approve, reject).
         *
         * @param id                            The ID of the company to update.
         * @param companyUpdateStatusRequestDto The DTO containing the new status and a
         *                                      rejection reason if applicable.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> updateCompanyStatus(@PathVariable Long id,
                        @RequestBody CompanyUpdateStatusRequestDto companyUpdateStatusRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.updateCompanyStatus(id, companyUpdateStatusRequestDto)));
        }

        /**
         * Endpoint to get a company's public details by its ID.
         *
         * @param id The ID of the company to retrieve.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         company's public details.
         */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<CompanyResponseDto>> getCompanyById(@PathVariable Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.getCompanyById(id)));
        }

        /**
         * Endpoint to list all companies in the system.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of all company DTOs.
         */
        @GetMapping("/all")
        public ResponseEntity<ApiResponse<List<CompanyResponseDto>>> listAllCompanies() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                companyService.listAllCompanies()));
        }
}
