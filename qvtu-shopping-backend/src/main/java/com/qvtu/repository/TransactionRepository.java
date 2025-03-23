package com.qvtu.repository;

import com.qvtu.model.Transaction;
import com.qvtu.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * 根据支付ID查询交易记录列表
     * @param paymentId 支付ID
     * @return 交易记录列表
     */
    List<Transaction> findByPaymentId(Long paymentId);
    
    /**
     * 根据交易类型查询交易记录列表
     * @param type 交易类型
     * @return 交易记录列表
     */
    List<Transaction> findByType(TransactionType type);
    
    /**
     * 根据幂等键查询交易记录
     * @param idempotencyKey 幂等键
     * @return 交易记录
     */
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    
    /**
     * 根据提供商交易ID查询交易记录
     * @param providerTransactionId 提供商交易ID
     * @return 交易记录
     */
    Optional<Transaction> findByProviderTransactionId(String providerTransactionId);
} 