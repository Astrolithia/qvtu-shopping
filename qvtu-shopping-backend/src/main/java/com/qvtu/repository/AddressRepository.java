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
     * 根据客户ID查询默认配送地址
     * @param customerId 客户ID
     * @return 默认配送地址
     */
    Optional<Address> findByCustomerIdAndIsDefaultShippingTrue(Long customerId);
    
    /**
     * 根据客户ID查询默认账单地址
     * @param customerId 客户ID
     * @return 默认账单地址
     */
    Optional<Address> findByCustomerIdAndIsDefaultBillingTrue(Long customerId);
    
    /**
     * 根据客户ID查询同时是默认配送和默认账单的地址
     * @param customerId 客户ID
     * @return 同时是默认配送和默认账单的地址
     */
    Optional<Address> findByCustomerIdAndIsDefaultShippingTrueAndIsDefaultBillingTrue(Long customerId);
} 