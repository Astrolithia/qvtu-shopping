package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(unique = true)
    private String sku;
    
    @Column(name = "barcode")
    private String barcode;
    
    @Column(name = "ean")
    private String ean;
    
    @Column(name = "upc")
    private String upc;
    
    @Column(precision = 19, scale = 4)
    private BigDecimal price;
    
    @Column(name = "compare_at_price", precision = 19, scale = 4)
    private BigDecimal compareAtPrice;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionValue> optionValues = new ArrayList<>();
    
    @Column(name = "inventory_quantity", nullable = false)
    private Integer inventoryQuantity = 0;
    
    @Column(name = "allow_backorder")
    private Boolean allowBackorder = false;
    
    @Column(name = "manage_inventory")
    private Boolean manageInventory = true;
    
    @Column(name = "weight")
    private Double weight;
    
    @Column(name = "length")
    private Double length;
    
    @Column(name = "height")
    private Double height;
    
    @Column(name = "width")
    private Double width;
    
    @Column(name = "origin_country")
    private String originCountry;
    
    @Column(name = "mid_code")
    private String midCode;
    
    @Column(name = "material")
    private String material;
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
} 