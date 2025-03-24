package com.qvtu.repository;

import com.qvtu.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * 根据电子邮件查找客户
     * @param email 电子邮件
     * @return 客户
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * 根据电话号码查找客户
     * @param phone 电话号码
     * @return 客户
     */
    Optional<Customer> findByPhone(String phone);
    
    /**
     * 根据用户ID查找客户
     * @param userId 用户ID
     * @return 客户
     */
    @Query("SELECT c FROM Customer c WHERE c.id = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);
    
    /**
     * 模糊搜索客户
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 客户分页结果
     */
    Page<Customer> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email, Pageable pageable);

    boolean existsByEmail(String email);
} 