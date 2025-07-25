package com.online_store.backend.api.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
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
import com.online_store.backend.api.shipper.repository.ShipperRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
        private final ProductRepository productRepository;
        private final ProductUtilsService productUtilsService;
        private final CreateProductMapper createProductMapper;
        private final GetProductDetailsMapper getProductDetailsMapper;
        private final GetProductMapper getProductMapper;
        private final CompanyUtilsService companyUtilsService;
        private final BrandRepository brandRepository;
        private final ShipperRepository shipperRepository;
        private final CategoryRepository categoryRepository;

        @Transactional
        public String addProduct(ProductRequestDto productRequestDto,
                        MultipartHttpServletRequest request) {
                Company company = companyUtilsService.getCurrentUserCompany();
                if (company.getStatus() != CompanyStatus.APPROVED) {
                        throw new Error("Your company not approved!");
                }
                Brand brand = productUtilsService.findEntityById(brandRepository::findById,
                                productRequestDto.getBrand(),
                                "Brand");
                Shipper shipper = productUtilsService.findEntityById(shipperRepository::findById,
                                productRequestDto.getShipper(),
                                "Shipper");
                Category category = productUtilsService.findEntityById(categoryRepository::findById,
                                productRequestDto.getCategory(),
                                "Category");

                Map<String, List<MultipartFile>> dynamicFiles = productUtilsService.processDynamicFiles(request);

                Product product = createProductMapper.productMapper(
                                productRequestDto,
                                dynamicFiles,
                                company,
                                shipper,
                                brand,
                                category);

                productRepository.save(product);

                return "Product created successfully.";
        }

        @Transactional(readOnly = true)
        public ProductDetailsResponseDto getProductById(Long productId) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
                return getProductDetailsMapper.productDetailResponseMapper(product);
        }

        public List<ProductResponseDto> listProducts() {
                List<Product> products = productRepository.findAll();

                return products.stream().map(getProductMapper::prouctMapper).toList();
        }
}