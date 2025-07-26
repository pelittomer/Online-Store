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

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createCompany(
            @Valid @RequestPart("company") CompanyRequestDto companyRequestDto,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.createCompany(companyRequestDto, file)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateMyCompany(
            @Valid @RequestPart("company") CompanyUpdateRequestDto companyUpdateRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.updateMyCompany(companyUpdateRequestDto, file)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CompanyPrivateResponseDto>> getMyCompany() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.getMyCompany()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateCompanyStatus(@PathVariable Long id,
            @RequestBody CompanyUpdateStatusRequestDto companyUpdateStatusRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.updateCompanyStatus(id, companyUpdateStatusRequestDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.getCompanyById(id)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CompanyResponseDto>>> listAllCompanies() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        companyService.listAllCompanies()));
    }
}
