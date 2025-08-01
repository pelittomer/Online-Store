package com.online_store.backend.api.product.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.brand.utils.mapper.GetBrandMapper;
import com.online_store.backend.api.category.utils.mapper.GetCategoryMapper;
import com.online_store.backend.api.company.utils.mapper.GetCompanyMapper;
import com.online_store.backend.api.product.dto.base.DiscountDto;
import com.online_store.backend.api.product.dto.base.FeatureDto;
import com.online_store.backend.api.product.dto.response.CriteriaOptionResponseDto;
import com.online_store.backend.api.product.dto.response.ProductCriteriaResponseDto;
import com.online_store.backend.api.product.dto.response.ProductDetailsDetailResponseDto;
import com.online_store.backend.api.product.dto.response.ProductDetailsResponseDto;
import com.online_store.backend.api.product.dto.response.ProductStockResponseDto;
import com.online_store.backend.api.product.dto.response.ProductVariationDto;
import com.online_store.backend.api.product.dto.response.ProductVariationOptionDto;
import com.online_store.backend.api.product.dto.response.StockVariationResponseDto;
import com.online_store.backend.api.product.entities.CriteriaOption;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.ProductCriteria;
import com.online_store.backend.api.product.entities.ProductDetail;
import com.online_store.backend.api.product.entities.ProductStock;
import com.online_store.backend.api.product.entities.embeddables.Discount;
import com.online_store.backend.api.product.entities.embeddables.Feature;
import com.online_store.backend.api.product.entities.embeddables.StockVariation;
import com.online_store.backend.api.shipper.utils.mapper.GetShipperMapper;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;
import com.online_store.backend.api.variation.utils.mapper.GetVariationMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProductDetailsMapper {
        private final GetBrandMapper getBrandMapper;
        private final GetShipperMapper getShipperMapper;
        private final GetCategoryMapper getCategoryMapper;
        private final GetCompanyMapper getCompanyMapper;
        private final GetVariationMapper getVariationMapper;

        public ProductDetailsResponseDto productDetailResponseMapper(Product dto) {
                DiscountDto discount = null;
                if (dto.getDiscount() != null) {
                        discount = discountMapper(dto.getDiscount());
                }

                List<Long> images = dto.getImages().stream()
                                .map((image) -> image.getId()).toList();

                ProductDetailsDetailResponseDto productDetail = productDetailMapper(dto.getProductDetail());

                List<ProductStockResponseDto> productStocks = dto.getProductStocks().stream()
                                .map(this::productStocksMapper).toList();

                return ProductDetailsResponseDto.builder()
                                .id(dto.getId())
                                .name(dto.getName())
                                .discount(discount)
                                .isPublished(dto.getIsPublished())
                                .images(images)
                                .brand(getBrandMapper.brandMapper(dto.getBrand()))
                                .shipper(getShipperMapper.shipperMapper(dto.getShipper()))
                                .company(getCompanyMapper.companyMapper(dto.getCompany()))
                                .category(getCategoryMapper.mapCategoryToResponseDto(dto.getCategory()))
                                .productDetail(productDetail)
                                .productStocks(productStocks)
                                .createdAt(dto.getCreatedAt())
                                .updatedAt(dto.getUpdatedAt())
                                .build();
        }

        private DiscountDto discountMapper(Discount dto) {
                return DiscountDto.builder().discountPercentage(dto.getDiscountPercentage())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getStartDate())
                                .appliedPrice(dto.getAppliedPrice())
                                .build();
        }

        private ProductDetailsDetailResponseDto productDetailMapper(ProductDetail dto) {
                List<FeatureDto> features = dto.getFeatures().stream()
                                .map(this::featureMapper).toList();

                List<ProductCriteriaResponseDto> productCriterias = dto.getProductCriterias().stream()
                                .map(this::productCriteriaMapper).toList();
                return ProductDetailsDetailResponseDto.builder()
                                .id(dto.getId())
                                .description(dto.getDescription())
                                .shortDescription(dto.getShortDescription())
                                .features(features)
                                .productCriterias(productCriterias)
                                .build();
        }

        private FeatureDto featureMapper(Feature dto) {
                return FeatureDto.builder()
                                .name(dto.getName())
                                .value(dto.getValue())
                                .build();
        }

        private ProductCriteriaResponseDto productCriteriaMapper(ProductCriteria dto) {
                List<CriteriaOptionResponseDto> criteriaOptions = dto.getCriteriaOptions().stream()
                                .map(this::criteriaOptionsMapper).toList();
                ProductVariationDto variation = productVariationMapper(dto.getVariation());
                return ProductCriteriaResponseDto.builder()
                                .id(dto.getId())
                                .variation(variation)
                                .criteriaOptions(criteriaOptions)
                                .build();
        }

        private ProductVariationDto productVariationMapper(Variation dto) {
                return ProductVariationDto.builder()
                                .id(dto.getId())
                                .variation(dto.getName())
                                .build();
        }

        private CriteriaOptionResponseDto criteriaOptionsMapper(CriteriaOption dto) {
                List<Long> images = dto.getImages().stream().map((image) -> image.getId()).toList();
                ProductVariationOptionDto variationOption = variationOptionMapper(dto.getVariationOption());
                return CriteriaOptionResponseDto.builder()
                                .id(dto.getId())
                                .variationOption(variationOption)
                                .images(images)
                                .build();
        }

        private ProductVariationOptionDto variationOptionMapper(VariationOption dto) {
                return ProductVariationOptionDto.builder()
                                .id(dto.getId())
                                .variationOption(dto.getName())
                                .build();
        }

        private ProductStockResponseDto productStocksMapper(ProductStock dto) {
                List<StockVariationResponseDto> stockVariations = dto.getStockVariations().stream()
                                .map(this::stockVariationMapper).toList();
                return ProductStockResponseDto.builder()
                                .id(dto.getId())
                                .stockQuantity(dto.getStockQuantity())
                                .additionalPrice(dto.getAdditionalPrice())
                                .isLimited(dto.getIsLimited())
                                .replenishQuantity(dto.getReplenishQuantity())
                                .stockVariations(stockVariations)
                                .build();
        }

        public StockVariationResponseDto stockVariationMapper(StockVariation dto) {
                return StockVariationResponseDto.builder()
                                .id(dto.getVariation().getId())
                                .variation(dto.getVariation().getName())
                                .variationOption(getVariationMapper.variationOptionMapper(dto.getVariationOption()))
                                .build();
        }
}
