package com.qvtu.repository;

import com.qvtu.model.InventoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryLevelRepository extends JpaRepository<InventoryLevel, Long> {
    
    /**
     * 根据库存项ID和库存地点ID查询库存级别
     * @param inventoryItemId 库存项ID
     * @param locationId 库存地点ID
     * @return 库存级别
     */
    Optional<InventoryLevel> findByInventoryItemIdAndLocationId(Long inventoryItemId, Long locationId);
    
    /**
     * 根据库存项ID查询库存级别列表
     * @param inventoryItemId 库存项ID
     * @return 库存级别列表
     */
    List<InventoryLevel> findByInventoryItemId(Long inventoryItemId);
    
    /**
     * 根据库存地点ID查询库存级别列表
     * @param locationId 库存地点ID
     * @return 库存级别列表
     */
    List<InventoryLevel> findByLocationId(Long locationId);
    
    /**
     * 增加库存数量
     * @param inventoryItemId 库存项ID
     * @param locationId 库存地点ID
     * @param quantity 增加的数量
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE InventoryLevel il SET il.stockLevel = il.stockLevel + :quantity " +
            "WHERE il.inventoryItem.id = :inventoryItemId AND il.location.id = :locationId")
    int incrementStockLevel(@Param("inventoryItemId") Long inventoryItemId, 
                           @Param("locationId") Long locationId, 
                           @Param("quantity") int quantity);
    
    /**
     * 减少库存数量
     * @param inventoryItemId 库存项ID
     * @param locationId 库存地点ID
     * @param quantity 减少的数量
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE InventoryLevel il SET il.stockLevel = il.stockLevel - :quantity " +
            "WHERE il.inventoryItem.id = :inventoryItemId AND il.location.id = :locationId " +
            "AND il.stockLevel >= :quantity")
    int decrementStockLevel(@Param("inventoryItemId") Long inventoryItemId, 
                           @Param("locationId") Long locationId, 
                           @Param("quantity") int quantity);
    
    /**
     * 预留库存
     * @param inventoryItemId 库存项ID
     * @param locationId 库存地点ID
     * @param quantity 预留的数量
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE InventoryLevel il SET il.reservedQuantity = il.reservedQuantity + :quantity " +
            "WHERE il.inventoryItem.id = :inventoryItemId AND il.location.id = :locationId " +
            "AND (il.stockLevel - il.reservedQuantity) >= :quantity")
    int reserveStock(@Param("inventoryItemId") Long inventoryItemId, 
                    @Param("locationId") Long locationId, 
                    @Param("quantity") int quantity);
} 