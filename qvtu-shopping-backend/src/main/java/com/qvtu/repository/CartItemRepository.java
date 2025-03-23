package com.qvtu.repository;

import com.qvtu.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    /**
     * 根据购物车ID查询购物车项列表
     * @param cartId 购物车ID
     * @return 购物车项列表
     */
    List<CartItem> findByCartId(Long cartId);
    
    /**
     * 根据购物车ID和产品变体ID查询购物车项
     * @param cartId 购物车ID
     * @param variantId 产品变体ID
     * @return 购物车项
     */
    Optional<CartItem> findByCartIdAndVariantId(Long cartId, Long variantId);
    
    /**
     * 更新购物车项数量
     * @param cartItemId 购物车项ID
     * @param quantity 数量
     * @return 受影响的行数
     */
    @Modifying
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.id = :cartItemId")
    int updateQuantity(@Param("cartItemId") Long cartItemId, @Param("quantity") int quantity);
    
    /**
     * 删除购物车中的所有项
     * @param cartId 购物车ID
     * @return 受影响的行数
     */
    int deleteByCartId(Long cartId);
} 