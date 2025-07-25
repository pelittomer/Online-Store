package com.online_store.backend.api.review.utils.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.entities.OrderStatus;
import com.online_store.backend.api.order.repository.OrderItemRepository;
import com.online_store.backend.api.order.repository.OrderRepository;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.review.dto.request.ReviewRequestDto;
import com.online_store.backend.api.review.entities.Review;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReviewMapper {
    private final OrderItemRepository orderItemRepository;
    private final UploadService uploadService;

    public Review reviewMapper(ReviewRequestDto dto,
            Product product,
            List<MultipartFile> imageFiles,
            User user) {

        Boolean isPurchased = false;//mock

        Set<Upload> images = imageFiles.stream()
                .map(uploadService::createFile).collect(Collectors.toSet());

        return Review.builder()
                .comment(dto.getComment())
                .rate(dto.getRate())
                .isPurchased(isPurchased)
                .images(images)
                .user(user)
                .product(product)
                .build();
    }
}
