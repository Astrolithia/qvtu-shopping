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
@Table(name = "carts")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "billing_address_id")
    private Long billingAddressId;
    
    @Column(name = "shipping_address_id")
    private Long shippingAddressId;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    
    @Column(name = "payment_id")
    private Long paymentId;
    
    @Column(name = "region_id")
    private Long regionId;
    
    @Column(name = "shipping_method_id")
    private Long shippingMethodId;
    
    @Column(name = "subtotal", precision = 19, scale = 4)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_total", precision = 19, scale = 4)
    private BigDecimal taxTotal = BigDecimal.ZERO;
    
    @Column(name = "shipping_total", precision = 19, scale = 4)
    private BigDecimal shippingTotal = BigDecimal.ZERO;
    
    @Column(name = "discount_total", precision = 19, scale = 4)
    private BigDecimal discountTotal = BigDecimal.ZERO;
    
    @Column(name = "total", precision = 19, scale = 4)
    private BigDecimal total = BigDecimal.ZERO;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "payment_authorized_at")
    private LocalDateTime paymentAuthorizedAt;
    
    @Column(name = "idempotency_key")
    private String idempotencyKey;
    
    @Column(name = "context")
    private String context;
    
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