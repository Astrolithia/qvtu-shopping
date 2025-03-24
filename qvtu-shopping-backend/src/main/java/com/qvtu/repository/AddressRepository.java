package com.qvtu.repository;

import com.qvtu.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
     * 根据客户ID查询地址列表
     * @param customerId 客户ID
     * @return 地址列表
     */
    List<Address> findByCustomerId(Long customerId);
    
    /**
     * 根据客户ID查询默认地址
     * @param customerId 客户ID
     * @return 默认地址
     */
    Optional<Address> findByCustomerIdAndIsDefaultTrue(Long customerId);
} 