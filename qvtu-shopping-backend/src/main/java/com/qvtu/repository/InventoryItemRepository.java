package com.qvtu.repository;

import com.qvtu.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    
    /**
     * 根据SKU查询库存项
     * @param sku SKU
     * @return 库存项
     */
    Optional<InventoryItem> findBySku(String sku);
    
    /**
     * 根据产品变体ID查询库存项
     * @param variantId 产品变体ID
     * @return 库存项
     */
    Optional<InventoryItem> findByVariantId(Long variantId);
} 