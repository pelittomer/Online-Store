package com.online_store.backend.api.review.entities;

import java.util.HashSet;
import java.util.Set;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.user.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "product_id" })
})
@ToString(exclude = { "user", "product", "images" })
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer rate;

    @Column(name = "is_purchased", nullable = false)
    private Boolean isPurchased;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"), inverseJoinColumns = @JoinColumn(name = "upload_id"))
    @Builder.Default
    private Set<Upload> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}