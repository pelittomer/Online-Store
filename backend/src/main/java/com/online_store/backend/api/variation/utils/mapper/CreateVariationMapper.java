package com.online_store.backend.api.variation.utils.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateVariationMapper {

    public Variation variationMapper(VariationRequestDto dto, Category category) {
        Set<VariationOption> variationOptions = dto.getOptions().stream()
                .map(this::variationOptionMapper).collect(Collectors.toSet());

        Variation variation = Variation.builder()
                .name(dto.getName())
                .category(category)
                .options(variationOptions)
                .build();

        variationOptions.forEach(option -> option.setVariation(variation));

        return variation;
    }

    public VariationOption variationOptionMapper(String name) {
        return VariationOption.builder()
                .name(name)
                .build();
    }
}
