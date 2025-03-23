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
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name = "email")
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;
    
    @Column(name = "fulfillment_status")
    private String fulfillmentStatus;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    
    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;
    
    @Column(name = "shipping_method_id")
    private Long shippingMethodId;
    
    @Column(name = "shipping_price", precision = 19, scale = 4)
    private BigDecimal shippingPrice;
    
    @Column(name = "subtotal", precision = 19, scale = 4)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_total", precision = 19, scale = 4)
    private BigDecimal taxTotal = BigDecimal.ZERO;
    
    @Column(name = "discount_total", precision = 19, scale = 4)
    private BigDecimal discountTotal = BigDecimal.ZERO;
    
    @Column(name = "total", precision = 19, scale = 4)
    private BigDecimal total = BigDecimal.ZERO;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode = "CNY";
    
    @Column(name = "region_id")
    private Long regionId;
    
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    
    @Column(name = "no_notification")
    private Boolean noNotification;
    
    @Column(name = "idempotency_key")
    private String idempotencyKey;
    
    @Column(name = "external_id")
    private String externalId;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();
    
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
    
    // 添加订单项
    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
    
    // 移除订单项
    public void removeOrderItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
    
    // 计算订单总额
    public void calculateTotals() {
        // 计算小计
        this.subtotal = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算最终总额
        this.total = this.subtotal
                .add(this.shippingPrice != null ? this.shippingPrice : BigDecimal.ZERO)
                .add(this.taxTotal != null ? this.taxTotal : BigDecimal.ZERO)
                .subtract(this.discountTotal != null ? this.discountTotal : BigDecimal.ZERO);
    }
} 