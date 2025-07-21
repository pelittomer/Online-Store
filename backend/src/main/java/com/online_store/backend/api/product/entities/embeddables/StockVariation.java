package com.online_store.backend.api.product.entities.embeddables;

import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString(exclude = {"variation","variationOption"})
public class StockVariation {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", nullable = false)
    private Variation variation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_option_id", nullable = false)
    private VariationOption variationOption;
}
