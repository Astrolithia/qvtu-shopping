package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_levels", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inventory_item_id", "location_id"})
})
public class InventoryLevel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;
    
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private StockLocation location;
    
    @Column(name = "stock_level", nullable = false)
    private Integer stockLevel = 0;
    
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;
    
    @Column(name = "incoming_quantity", nullable = false)
    private Integer incomingQuantity = 0;
    
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
    
    /**
     * 获取当前可用库存量
     * @return 可用库存量
     */
    @Transient
    public Integer getAvailableQuantity() {
        return this.stockLevel - this.reservedQuantity;
    }
} 