package com.qvtu.repository;

import com.qvtu.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * 根据订单ID查询订单项列表
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> findByOrderId(Long orderId);
    
    /**
     * 根据产品变体ID查询订单项列表
     * @param variantId 产品变体ID
     * @return 订单项列表
     */
    List<OrderItem> findByVariantId(Long variantId);
    
    /**
     * 查询产品的销售数量
     * @param productId 产品ID
     * @return 销售数量
     */
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi " +
            "JOIN oi.variant v " +
            "JOIN v.product p " +
            "WHERE p.id = :productId")
    Long countSalesByProductId(@Param("productId") Long productId);
    
    /**
     * 查询产品变体的销售数量
     * @param variantId 产品变体ID
     * @return 销售数量
     */
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.variant.id = :variantId")
    Long countSalesByVariantId(@Param("variantId") Long variantId);
} 