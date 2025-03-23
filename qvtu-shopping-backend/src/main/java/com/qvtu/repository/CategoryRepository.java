package com.qvtu.repository;

import com.qvtu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据名称查询分类
     * @param name 分类名称
     * @return 分类
     */
    Optional<Category> findByName(String name);
    
    /**
     * 查询所有顶级分类
     * @return 所有顶级分类
     */
    List<Category> findByParentIsNull();
    
    /**
     * 根据父分类ID查询子分类
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> findByParentId(Long parentId);
    
    /**
     * 检查是否存在子分类
     * @param categoryId 分类ID
     * @return 是否存在子分类
     */
    boolean existsByParentId(Long categoryId);
    
    /**
     * 查询分类树
     * @return 分类树
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL")
    List<Category> findCategoryTree();
} 