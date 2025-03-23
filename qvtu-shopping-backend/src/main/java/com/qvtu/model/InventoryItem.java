package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "inventory_items")
public class InventoryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sku", unique = true)
    private String sku;
    
    @Column(name = "origin_country")
    private String originCountry;
    
    @Column(name = "hs_code")
    private String hsCode;
    
    @Column(name = "mid_code")
    private String midCode;
    
    @Column(name = "material")
    private String material;
    
    @Column(name = "weight")
    private Double weight;
    
    @Column(name = "length")
    private Double length;
    
    @Column(name = "height")
    private Double height;
    
    @Column(name = "width")
    private Double width;
    
    @OneToOne
    @JoinColumn(name = "variant_id", unique = true)
    private ProductVariant variant;
    
    @OneToMany(mappedBy = "inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryLevel> inventoryLevels = new ArrayList<>();
    
    @OneToMany(mappedBy = "inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();
    
    @Column(name = "requires_shipping")
    private Boolean requiresShipping = true;
    
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