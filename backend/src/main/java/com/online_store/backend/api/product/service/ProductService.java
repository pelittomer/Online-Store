package com.online_store.backend.api.product.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.utils.BrandUtilsService;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.entities.CompanyStatus;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.product.dto.request.ProductRequestDto;
import com.online_store.backend.api.product.dto.response.ProductDetailsResponseDto;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.product.utils.ProductUtilsService;
import com.online_store.backend.api.product.utils.mapper.CreateProductMapper;
import com.online_store.backend.api.product.utils.mapper.GetProductDetailsMapper;
import com.online_store.backend.api.product.utils.mapper.GetProductMapper;
import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.utils.ShipperUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing products.
 * This service handles the business logic for adding, retrieving, and listing
 * products.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
        // repositories
        private final ProductRepository productRepository;
        // utils
        private final ProductUtilsService productUtilsService;
        private final CompanyUtilsService companyUtilsService;
        private final BrandUtilsService brandUtilsService;
        private final ShipperUtilsService shipperUtilsService;
        private final CategoryUtilsService categoryUtilsService;
        // mappers
        private final CreateProductMapper createProductMapper;
        private final GetProductDetailsMapper getProductDetailsMapper;
        private final GetProductMapper getProductMapper;

        /**
         * Adds a new product to the system.
         * This method validates that the current user's company is approved before
         * creating the product.
         * It also processes dynamic file uploads for product images and associates the
         * product with its
         * brand, shipper, and category.
         *
         * @param dto     The DTO containing the product's details.
         * @param request The {@link MultipartHttpServletRequest} containing the
         *                uploaded product images.
         * @return A success message upon successful product creation.
         * @see com.online_store.backend.api.product.controller.ProductController#addProduct(ProductRequestDto,
         *      MultipartHttpServletRequest)
         */
        @Transactional
        public String addProduct(ProductRequestDto dto,
                        MultipartHttpServletRequest request) {
                log.info("Adding new product for company.");

                Company company = companyUtilsService.getCurrentUserCompany();
                if (company.getStatus() != CompanyStatus.APPROVED) {
                        log.warn("Attempt to add product by a non-approved company: {}", company.getName());
                        throw new Error("Your company is not approved. Products can't be added.");
                }

                Brand brand = brandUtilsService.findBrandById(dto.getBrand());
                Shipper shipper = shipperUtilsService.findShipperById(dto.getShipper());
                Category category = categoryUtilsService.findCategoryById(dto.getCategory());

                Map<String, List<MultipartFile>> dynamicFiles = processDynamicFiles(request);

                Product product = createProductMapper.productMapper(
                                dto,
                                dynamicFiles,
                                company,
                                shipper,
                                brand,
                                category);

                productRepository.save(product);

                log.info("Product '{}' successfully added by company '{}'.", product.getName(), company.getName());
                return "Product created successfully.";
        }

        /**
         * Retrieves the detailed information of a specific product by its ID.
         *
         * @param productId The ID of the product to retrieve.
         * @return A {@link ProductDetailsResponseDto} containing the product's details.
         * @see com.online_store.backend.api.product.controller.ProductController#getProductById(Long)
         */
        @Transactional(readOnly = true)
        public ProductDetailsResponseDto getProductById(Long productId) {
                log.info("Fetching details for product with ID: {}", productId);
                Product product = productUtilsService.findProductById(productId);
                return getProductDetailsMapper.productDetailResponseMapper(product);
        }

        /**
         * Retrieves a list of all products.
         *
         * @return A list of {@link ProductResponseDto} for all products.
         * @see com.online_store.backend.api.product.controller.ProductController#listProducts()
         */
        @Transactional(readOnly = true)
        public List<ProductResponseDto> listProducts() {
                log.info("Listing all products.");
                List<Product> products = productRepository.findAll();
                return products.stream().map(getProductMapper::prouctMapper).toList();
        }

        /**
         * Processes dynamic multipart files from a request.
         * It iterates through all file names in the request and collects the
         * corresponding
         * non-empty files into a map.
         *
         * @param request The {@link MultipartHttpServletRequest} containing the files.
         * @return A map where the key is the file name and the value is a list of
         *         {@link MultipartFile}s.
         * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
         *      MultipartHttpServletRequest)
         */
        private Map<String, List<MultipartFile>> processDynamicFiles(MultipartHttpServletRequest request) {
                Map<String, List<MultipartFile>> dynamicFiles = new HashMap<>();
                Iterator<String> fileNames = request.getFileNames();

                while (fileNames.hasNext()) {
                        String fileName = fileNames.next();
                        List<MultipartFile> filesForThisKey = request.getFiles(fileName);

                        if (filesForThisKey != null && !filesForThisKey.isEmpty()) {
                                List<MultipartFile> actualFiles = filesForThisKey.stream()
                                                .filter(f -> !f.isEmpty())
                                                .toList();

                                if (!actualFiles.isEmpty()) {
                                        dynamicFiles.put(fileName, actualFiles);
                                }
                        }
                }
                return dynamicFiles;
        }
}