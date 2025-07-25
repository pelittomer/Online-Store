package com.online_store.backend.api.cart.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.cart.dto.response.CartItemResponseDto;
import com.online_store.backend.api.cart.dto.response.CartProductResponseDto;
import com.online_store.backend.api.cart.dto.response.CartResponseDto;
import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.entities.CartItem;
import com.online_store.backend.api.product.dto.base.DiscountDto;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.entities.embeddables.Discount;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCartMapper {

        public CartResponseDto cartMapper(Cart dto) {
                List<CartItemResponseDto> carItems = dto.getCartItems().stream()
                                .map(this::carItemMapper).toList();
                return CartResponseDto.builder()
                                .id(dto.getId())
                                .cartItems(carItems)
                                .createdAt(dto.getCreatedAt())
                                .updatedAt(dto.getUpdatedAt())
                                .build();
        }

        private CartItemResponseDto carItemMapper(CartItem dto) {
                CartProductResponseDto product = productMapper(dto.getProduct());
                return CartItemResponseDto.builder()
                                .id(dto.getId())
                                .quantity(dto.getQuantity())
                                .product(product)
                                .productStock(dto.getProductStock().getId())
                                .build();
        }

        private CartProductResponseDto productMapper(Product dto) {
                List<Long> images = dto.getImages().stream()
                                .map((image) -> image.getId()).toList();
                DiscountDto discount = null;
                if (dto.getDiscount() != null) {
                        discount = discountMapper(dto.getDiscount());
                }
                return CartProductResponseDto.builder()
                                .id(dto.getId())
                                .name(dto.getName())
                                .discount(discount)
                                .images(images)
                                .isPublished(dto.getIsPublished())
                                .brand(dto.getBrand().getName())
                                .company(dto.getCompany().getName())
                                .shipper(dto.getShipper().getName())
                                .build();
        }

        private DiscountDto discountMapper(Discount dto) {
                return DiscountDto.builder().discountPercentage(dto.getDiscountPercentage())
                                .startDate(dto.getStartDate())
                                .endDate(dto.getStartDate())
                                .appliedPrice(dto.getAppliedPrice())
                                .build();
        }
}
