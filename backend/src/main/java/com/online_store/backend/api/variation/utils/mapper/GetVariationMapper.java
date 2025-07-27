package com.online_store.backend.api.variation.utils.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetVariationMapper {

    public VariationResponseDto variationMapper(Variation dto) {
        List<VariationOptionResponseDto> variationOptions = dto.getOptions().stream()
                .map(this::variationOptionMapper)
                .collect(Collectors.toList());
        return VariationResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .options(variationOptions)
                .build();
    }

    public VariationOptionResponseDto variationOptionMapper(VariationOption dto) {
        return VariationOptionResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
