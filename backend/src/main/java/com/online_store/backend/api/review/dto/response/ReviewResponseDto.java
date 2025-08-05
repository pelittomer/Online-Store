package com.online_store.backend.api.review.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String comment;
    private Integer rate;
    private Boolean isPurchased;
    @Builder.Default
    private List<Long> images = new ArrayList<>();
    private Long product;
    private LocalDateTime createdAt;
}
