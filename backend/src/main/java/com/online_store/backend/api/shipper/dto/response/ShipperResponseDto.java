package com.online_store.backend.api.shipper.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipperResponseDto {
    private Long id;
    private String name;
    private Long logo;
    private String websiteUrl;
    private String phone;
    private String email;
    private String address;
    private boolean isActive;
    private LocalDateTime createdAt;
}
