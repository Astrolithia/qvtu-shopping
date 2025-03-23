package com.qvtu.repository;

import com.qvtu.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    
    /**
     * 根据产品ID查询产品变体
     * @param productId 产品ID
     * @return 产品变体列表
     */
    List<ProductVariant> findByProductId(Long productId);
    
    /**
     * 根据SKU查询产品变体
     * @param sku SKU
     * @return 产品变体
     */
    Optional<ProductVariant> findBySku(String sku);
    
    /**
     * 查询产品下有库存的变体
     * @param productId 产品ID
     * @return 有库存的产品变体列表
     */
    @Query("SELECT pv FROM ProductVariant pv " +
            "JOIN InventoryItem ii ON ii.variant.id = pv.id " +
            "JOIN InventoryLevel il ON il.inventoryItem.id = ii.id " +
            "WHERE pv.product.id = :productId AND il.stockLevel > 0")
    List<ProductVariant> findInStockVariantsByProductId(@Param("productId") Long productId);
} 