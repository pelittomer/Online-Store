package com.online_store.backend.api.product.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.product.entities.embeddables.StockVariation;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_stocks")
@ToString(exclude = { "product", "stockVariations" })
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stockQuantity;

    private BigDecimal additionalPrice;

    private Boolean isLimited;

    private Integer replenishQuantity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_stock_variation", joinColumns = @JoinColumn(name = "product_stock_id"))
    @Builder.Default
    private Set<StockVariation> stockVariations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
