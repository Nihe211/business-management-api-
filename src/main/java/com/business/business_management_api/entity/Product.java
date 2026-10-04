package com.business.business_management_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name="stock_quantity",nullable = false)
    private Integer stockQuantity;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive =true;
}
