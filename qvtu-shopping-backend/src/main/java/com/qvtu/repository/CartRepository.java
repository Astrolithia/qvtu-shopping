package com.qvtu.repository;

import com.qvtu.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    /**
     * 根据客户ID查询未完成的购物车
     * @param customerId 客户ID
     * @return 未完成的购物车
     */
    Optional<Cart> findByCustomerIdAndCompletedAtIsNull(Long customerId);
    
    /**
     * 根据客户ID查询所有购物车
     * @param customerId 客户ID
     * @return 购物车列表
     */
    List<Cart> findByCustomerId(Long customerId);
    
    /**
     * 根据邮箱查询未完成的购物车
     * @param email 邮箱
     * @return 未完成的购物车
     */
    Optional<Cart> findByEmailAndCompletedAtIsNull(String email);
    
    /**
     * 查询过期的购物车
     * @param expiryTime 过期时间
     * @return 过期的购物车列表
     */
    @Query("SELECT c FROM Cart c WHERE c.completedAt IS NULL AND c.updatedAt < :expiryTime")
    List<Cart> findExpiredCarts(@Param("expiryTime") LocalDateTime expiryTime);
} 