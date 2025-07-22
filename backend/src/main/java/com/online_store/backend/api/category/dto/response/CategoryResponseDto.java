package com.online_store.backend.api.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long image;
    private Long icon;
    private Integer leftValue;
    private Integer rightValue;
    private Long parent;
}
