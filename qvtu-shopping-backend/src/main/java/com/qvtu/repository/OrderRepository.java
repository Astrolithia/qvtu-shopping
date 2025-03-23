package com.qvtu.repository;

import com.qvtu.model.Order;
import com.qvtu.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单编号查询订单
     * @param orderNumber 订单编号
     * @return 订单
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    
    /**
     * 根据客户ID查询订单
     * @param customerId 客户ID
     * @param pageable 分页参数
     * @return 订单分页结果
     */
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * 根据邮箱查询订单
     * @param email 邮箱
     * @param pageable 分页参数
     * @return 订单分页结果
     */
    Page<Order> findByEmail(String email, Pageable pageable);
    
    /**
     * 根据状态查询订单
     * @param status 订单状态
     * @param pageable 分页参数
     * @return 订单分页结果
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    /**
     * 查询在指定时间段内创建的订单
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 订单列表
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 查询最近的订单
     * @param limit 查询数量
     * @return 最近的订单列表
     */
    @Query(value = "SELECT * FROM orders o ORDER BY o.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Order> findRecentOrders(@Param("limit") int limit);
    
    /**
     * 查询客户的最后一个订单
     * @param customerId 客户ID
     * @return 最后一个订单
     */
    Optional<Order> findFirstByCustomerIdOrderByCreatedAtDesc(Long customerId);
} 