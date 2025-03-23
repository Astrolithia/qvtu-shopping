package com.qvtu.repository;

import com.qvtu.model.Product;
import com.qvtu.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据状态查询产品
     * @param status 产品状态
     * @param pageable 分页参数
     * @return 产品分页结果
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    /**
     * 根据分类ID查询产品
     * @param categoryId 分类ID
     * @param pageable 分页参数
     * @return 产品分页结果
     */
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    /**
     * 模糊搜索产品
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 产品分页结果
     */
    Page<Product> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
    
    /**
     * 查询热门产品
     * @param limit 查询数量
     * @return 热门产品列表
     */
    @Query(value = "SELECT p.* FROM products p " +
            "JOIN product_variants pv ON p.id = pv.product_id " +
            "JOIN order_items oi ON oi.variant_id = pv.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(oi.id) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Product> findHotProducts(@Param("limit") int limit);
    
    /**
     * 查询新上架产品
     * @param pageable 分页参数
     * @return 新产品分页结果
     */
    Page<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status, Pageable pageable);
} 