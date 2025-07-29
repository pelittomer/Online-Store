package com.online_store.backend.api.category.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeDto {
    private Long id;
    private String name;
    private String description;
    private Long image;
    private Long icon;
    private List<CategoryTreeDto> children;
}
