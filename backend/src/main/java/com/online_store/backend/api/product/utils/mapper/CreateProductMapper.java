package com.online_store.backend.api.product.utils.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.product.dto.base.FeatureDto;
import com.online_store.backend.api.product.dto.request.CriteriaOptionRequestDto;
import com.online_store.backend.api.product.dto.request.ProductCriteriaRequestDto;
import com.online_store.backend.api.product.dto.request.ProductDetailRequestDto;
import com.online_store.backend.api.product.dto.request.ProductRequestDto;
import com.online_store.backend.api.product.dto.request.ProductStockRequestDto;
import com.online_store.backend.api.product.dto.request.StockVariationRequestDto;
import com.online_store.backend.api.product.entities.CriteriaOption;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.ProductCriteria;
import com.online_store.backend.api.product.entities.ProductDetail;
import com.online_store.backend.api.product.entities.ProductStock;
import com.online_store.backend.api.product.entities.embeddables.Feature;
import com.online_store.backend.api.product.entities.embeddables.StockVariation;
import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;
import com.online_store.backend.api.variation.repository.VariationOptionRepository;
import com.online_store.backend.api.variation.repository.VariationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateProductMapper {
        private final UploadService uploadService;
        private final VariationRepository variationRepository;
        private final VariationOptionRepository variationOptionRepository;

        public Product productMapper(ProductRequestDto dto,
                        Map<String, List<MultipartFile>> dynamicFiles,
                        Company company,
                        Shipper shipper,
                        Brand brand,
                        Category category) {
                Set<Upload> productImages = dynamicFiles.get("images").stream()
                                .map(uploadService::createFile).collect(Collectors.toSet());
                ProductDetail productDetail = productDetailMapper(dto.getProductDetail(), dynamicFiles);
                List<ProductStock> productStocks = dto.getProductStocks().stream()
                                .map(this::productStockMapper).collect(Collectors.toList());
                Product product = Product.builder()
                                .name(dto.getName())
                                .price(dto.getPrice())
                                .isPublished(dto.getIsPublished())
                                .images(productImages)
                                .brand(brand)
                                .shipper(shipper)
                                .company(company)
                                .category(category)
                                .productDetail(productDetail)
                                .productStocks(productStocks)
                                .build();
                productDetail.setProduct(product);
                productStocks.forEach((item) -> item.setProduct(product));
                return product;
        }

        private ProductDetail productDetailMapper(
                        ProductDetailRequestDto dto, Map<String, List<MultipartFile>> dynamicFiles) {
                Set<Feature> features = dto.getFeatures().stream().map(this::featureMapper)
                                .collect(Collectors.toSet());
                List<ProductCriteria> productCriterias = dto.getProductCriterias().stream()
                                .map((item) -> productCriteriaMapper(item, dynamicFiles)).toList();
                ProductDetail productDetail = ProductDetail.builder()
                                .description(dto.getDescription())
                                .shortDescription(dto.getShortDescription())
                                .features(features)
                                .productCriterias(productCriterias)
                                .build();
                productCriterias.forEach(item -> item.setProductDetail(productDetail));
                return productDetail;
        }

        private Feature featureMapper(FeatureDto dto) {
                return Feature.builder()
                                .name(dto.getName())
                                .value(dto.getValue())
                                .build();
        }

        private ProductCriteria productCriteriaMapper(ProductCriteriaRequestDto dto,
                        Map<String, List<MultipartFile>> dynamicFiles) {
                Set<CriteriaOption> criteriaOptions = dto.getCriteriaOptions().stream()
                                .map((item) -> criteriaOptionMapper(item, dynamicFiles))
                                .collect(Collectors.toSet());
                Variation variation = variationRepository.findById(dto.getVariation())
                                .orElseThrow(() -> new EntityNotFoundException("Variation not found!"));
                ProductCriteria productCriteria = ProductCriteria.builder()
                                .variation(variation)
                                .criteriaOptions(criteriaOptions)
                                .build();
                criteriaOptions.forEach(item -> item.setProductCriteria(productCriteria));
                return productCriteria;
        }

        private CriteriaOption criteriaOptionMapper(CriteriaOptionRequestDto dto,
                        Map<String, List<MultipartFile>> dynamicFiles) {
                VariationOption variationOption = variationOptionRepository.findById(dto.getVariationOption())
                                .orElseThrow(() -> new EntityNotFoundException("Variation option not found!"));
                Set<Upload> images = dynamicFiles.get(dto.getVariationOption().toString()).stream()
                                .map(uploadService::createFile).collect(Collectors.toSet());
                return CriteriaOption.builder()
                                .variationOption(variationOption)
                                .images(images)
                                .build();
        }

        private ProductStock productStockMapper(ProductStockRequestDto dto) {
                Set<StockVariation> stockVariations = dto.getStockVariations().stream()
                                .map(this::stockVariation).collect(Collectors.toSet());
                return ProductStock.builder()
                                .stockQuantity(dto.getStockQuantity())
                                .additionalPrice(dto.getAdditionalPrice())
                                .isLimited(dto.getIsLimited())
                                .replenishQuantity(dto.getReplenishQuantity())
                                .stockVariations(stockVariations)
                                .build();
        }

        private StockVariation stockVariation(StockVariationRequestDto dto) {
                Variation variation = variationRepository.findById(dto.getVariation())
                                .orElseThrow(() -> new EntityNotFoundException("Variation not found!"));

                VariationOption variationOption = variationOptionRepository.findById(dto.getVariationOption())
                                .orElseThrow(() -> new EntityNotFoundException("Variation option not found!"));
                return StockVariation.builder()
                                .variation(variation)
                                .variationOption(variationOption)
                                .build();
        }
}
