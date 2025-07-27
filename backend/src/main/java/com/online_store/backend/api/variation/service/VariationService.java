package com.online_store.backend.api.variation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.repository.VariationRepository;
import com.online_store.backend.api.variation.utils.VariationUtilsService;
import com.online_store.backend.api.variation.utils.mapper.CreateVariationMapper;
import com.online_store.backend.api.variation.utils.mapper.GetVariationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariationService {
    //repositories
    private final VariationRepository variationRepository;
    //utils
    private final VariationUtilsService variationUtilsService;
    //mappers
    private final CreateVariationMapper createVariationMapper;
    private final GetVariationMapper getVariationMapper;

    @Transactional
    public String addVariation(VariationRequestDto dto) {
        log.info("Adding new variation with name: '{}'", dto.getName());
        Category category = variationUtilsService.getCategoryById(dto.getCategory());
        Variation variation = createVariationMapper.variationMapper(dto, category);
        variationRepository.save(variation);

        log.info("Variation created successfully with ID: {}", variation.getId());
        return "Variation created successfully.";
    }

    @Transactional(readOnly = true)
    public List<VariationResponseDto> listVariations(Optional<Long> categoryId) {
        log.info("Listing variations. Category ID is present: {}", categoryId.isPresent());

        Stream<Variation> variationsStream = variationRepository.findByCategoryIsNull().stream();

        if (categoryId.isPresent()) {
            Category categoryOptional = variationUtilsService.getCategoryById(categoryId.get());
            if (categoryOptional != null) {
                variationsStream = Stream.concat(
                        variationsStream,
                        variationRepository.findByCategory(categoryOptional).stream());
            }
        }
        return variationsStream
                .map(getVariationMapper::variationMapper)
                .collect(Collectors.toList());
    }

}