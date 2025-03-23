package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
    
    @Column(name = "amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;
    
    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode = "CNY";
    
    @Column(name = "provider_transaction_id")
    private String providerTransactionId;
    
    @Column(name = "data", columnDefinition = "jsonb")
    private String data;
    
    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess = true;
    
    @Column(name = "idempotency_key")
    private String idempotencyKey;
    
    @Column(name = "error_message")
    private String errorMessage;
    
    @Column(name = "error_code")
    private String errorCode;
    
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 