package com.online_store.backend.api.brand.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDto {
    private Long id;
    private String name;
    private Long logo;
    private LocalDateTime createdAt;
}
