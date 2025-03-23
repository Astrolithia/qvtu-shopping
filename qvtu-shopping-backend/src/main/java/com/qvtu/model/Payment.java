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
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    @Column(name = "amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;
    
    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode = "CNY";
    
    @Column(name = "provider_id", nullable = false)
    private String providerId;
    
    @Column(name = "data", columnDefinition = "jsonb")
    private String data;
    
    @Column(name = "captured_at")
    private LocalDateTime capturedAt;
    
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
    
    @Column(name = "idempotency_key")
    private String idempotencyKey;
    
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 添加交易记录
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setPayment(this);
    }
    
    // 移除交易记录
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setPayment(null);
    }
} 