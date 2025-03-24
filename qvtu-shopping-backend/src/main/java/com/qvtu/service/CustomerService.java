package com.qvtu.service;

import com.qvtu.dto.AddressDTO;
import com.qvtu.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    
    /**
     * 根据ID查询客户
     * @param id 客户ID
     * @return 客户DTO
     */
    CustomerDTO findById(Long id);
    
    /**
     * 根据电子邮件查询客户
     * @param email 电子邮件
     * @return 客户DTO
     */
    Optional<CustomerDTO> findByEmail(String email);
    
    /**
     * 根据用户ID查询客户
     * @param userId 用户ID
     * @return 客户DTO
     */
    Optional<CustomerDTO> findByUserId(Long userId);
    
    /**
     * 分页查询所有客户
     * @param pageable 分页参数
     * @return 客户DTO分页结果
     */
    Page<CustomerDTO> findAll(Pageable pageable);
    
    /**
     * 搜索客户
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 客户DTO分页结果
     */
    Page<CustomerDTO> search(String query, Pageable pageable);
    
    /**
     * 创建客户
     * @param customerDTO 客户DTO
     * @return 创建后的客户DTO
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    
    /**
     * 更新客户信息
     * @param id 客户ID
     * @param customerDTO 客户DTO
     * @return 更新后的客户DTO
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    
    /**
     * 删除客户
     * @param id 客户ID
     */
    void deleteCustomer(Long id);
    
    /**
     * 获取客户的所有地址
     * @param customerId 客户ID
     * @return 地址DTO列表
     */
    List<AddressDTO> getAddressesByCustomerId(Long customerId);
    
    /**
     * 为客户添加地址
     * @param customerId 客户ID
     * @param addressDTO 地址DTO
     * @return 添加后的地址DTO
     */
    AddressDTO addAddress(Long customerId, AddressDTO addressDTO);
    
    /**
     * 更新客户地址
     * @param customerId 客户ID
     * @param addressId 地址ID
     * @param addressDTO 地址DTO
     * @return 更新后的地址DTO
     */
    AddressDTO updateAddress(Long customerId, Long addressId, AddressDTO addressDTO);
    
    /**
     * 删除客户地址
     * @param customerId 客户ID
     * @param addressId 地址ID
     */
    void deleteAddress(Long customerId, Long addressId);
    
    /**
     * 设置默认地址
     * @param customerId 客户ID
     * @param addressId 地址ID
     * @return 更新后的地址DTO
     */
    AddressDTO setDefaultAddress(Long customerId, Long addressId);
    
    /**
     * 更新客户群组
     * @param id 客户ID
     * @param groupIds 群组ID列表
     * @return 更新后的客户DTO
     */
    CustomerDTO updateCustomerGroups(Long id, List<Long> groupIds);
    
    /**
     * 获取客户的所有地址
     * @param customerId 客户ID
     * @return 地址DTO列表
     */
    List<AddressDTO> getCustomerAddresses(Long customerId);
    
    /**
     * 获取客户特定地址
     * @param customerId 客户ID
     * @param addressId 地址ID
     * @return 地址DTO
     */
    AddressDTO getCustomerAddress(Long customerId, Long addressId);
} 