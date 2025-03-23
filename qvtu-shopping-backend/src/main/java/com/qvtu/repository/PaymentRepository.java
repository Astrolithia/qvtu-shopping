package com.qvtu.repository;

import com.qvtu.model.Payment;
import com.qvtu.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * 根据订单ID查询支付记录列表
     * @param orderId 订单ID
     * @return 支付记录列表
     */
    List<Payment> findByOrderId(Long orderId);
    
    /**
     * 根据购物车ID查询支付记录列表
     * @param cartId 购物车ID
     * @return 支付记录列表
     */
    List<Payment> findByCartId(Long cartId);
    
    /**
     * 根据状态查询支付记录列表
     * @param status 支付状态
     * @return 支付记录列表
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * 根据幂等键查询支付记录
     * @param idempotencyKey 幂等键
     * @return 支付记录
     */
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
} 