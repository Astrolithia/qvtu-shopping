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
@Table(name = "cart_items")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", precision = 19, scale = 4)
    private BigDecimal unitPrice;
    
    @Column(name = "adjusted_unit_price", precision = 19, scale = 4)
    private BigDecimal adjustedUnitPrice;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "thumbnail")
    private String thumbnail;
    
    @Column(name = "is_giftcard")
    private Boolean isGiftcard = false;
    
    @Column(name = "should_merge")
    private Boolean shouldMerge = true;
    
    @Column(name = "allow_discounts")
    private Boolean allowDiscounts = true;
    
    @Column(name = "has_shipping")
    private Boolean hasShipping = false;
    
    @ElementCollection
    @CollectionTable(name = "cart_item_features", joinColumns = @JoinColumn(name = "cart_item_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 计算购物车项总价
     * @return 总价
     */
    @Transient
    public BigDecimal getSubtotal() {
        if (this.adjustedUnitPrice != null) {
            return this.adjustedUnitPrice.multiply(BigDecimal.valueOf(this.quantity));
        } else if (this.unitPrice != null) {
            return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
        return BigDecimal.ZERO;
    }
} 