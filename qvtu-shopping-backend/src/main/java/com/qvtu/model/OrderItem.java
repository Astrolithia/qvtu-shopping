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
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "thumbnail")
    private String thumbnail;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "fulfilled_quantity")
    private Integer fulfilledQuantity = 0;
    
    @Column(name = "shipped_quantity")
    private Integer shippedQuantity = 0;
    
    @Column(name = "returned_quantity")
    private Integer returnedQuantity = 0;
    
    @Column(name = "unit_price", precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "adjustments", columnDefinition = "jsonb")
    private String adjustments;
    
    @Column(name = "includes_tax")
    private Boolean includesTax = false;
    
    @Column(name = "tax_rate", precision = 10, scale = 2)
    private BigDecimal taxRate;
    
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
    
    @Column(name = "is_giftcard")
    private Boolean isGiftcard = false;
    
    @Column(name = "is_return")
    private Boolean isReturn = false;
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 计算订单项小计
     * @return 小计
     */
    @Transient
    public BigDecimal getSubtotal() {
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
    
    /**
     * 获取未发货数量
     * @return 未发货数量
     */
    @Transient
    public Integer getUnshippedQuantity() {
        return this.quantity - (this.shippedQuantity != null ? this.shippedQuantity : 0);
    }
} 