package com.online_store.backend.api.variation.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationResponseDto {
    private Long id;

    private String name;

    private List<VariationOptionResponseDto> options;
}
