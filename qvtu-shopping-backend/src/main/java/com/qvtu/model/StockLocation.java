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
@Table(name = "stock_locations")
public class StockLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "address_line_1")
    private String addressLine1;
    
    @Column(name = "address_line_2")
    private String addressLine2;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "country_code")
    private String countryCode;
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "phone")
    private String phone;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryLevel> inventoryLevels = new ArrayList<>();
    
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