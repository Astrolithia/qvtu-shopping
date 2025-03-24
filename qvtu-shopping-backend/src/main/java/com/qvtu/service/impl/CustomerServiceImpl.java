package com.qvtu.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qvtu.dto.AddressDTO;
import com.qvtu.dto.CustomerDTO;
import com.qvtu.dto.CustomerGroupDTO;
import com.qvtu.exception.EmailAlreadyExistsException;
import com.qvtu.exception.ResourceNotFoundException;
import com.qvtu.model.Address;
import com.qvtu.model.Customer;
import com.qvtu.model.CustomerGroup;
import com.qvtu.repository.AddressRepository;
import com.qvtu.repository.CustomerRepository;
import com.qvtu.repository.CustomerGroupRepository;
import com.qvtu.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return mapToDTO(customer);
    }
    
    @Override
    public Optional<CustomerDTO> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::mapToDTO);
    }
    
    @Override
    public Optional<CustomerDTO> findByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .map(this::mapToDTO);
    }
    
    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::mapToDTO);
    }
    
    @Override
    public Page<CustomerDTO> search(String query, Pageable pageable) {
        return customerRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(
                query, query, query, pageable)
                .map(this::mapToDTO);
    }
    
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // 检查邮箱是否已存在
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new EmailAlreadyExistsException(customerDTO.getEmail());
        }
        
        Customer customer = mapToEntity(customerDTO);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }
    
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhone(customerDTO.getPhone());
        customer.setAvatarUrl(customerDTO.getAvatarUrl());
        
        customer.setMetadata(customerDTO.getMetadata());
        
        Customer updatedCustomer = customerRepository.save(customer);
        
        return mapToDTO(updatedCustomer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customerRepository.delete(customer);
    }
    
    @Override
    public List<AddressDTO> getAddressesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        return customer.getAddresses().stream()
                .map(this::mapToAddressDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AddressDTO addAddress(Long customerId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = new Address();
        address.setCustomer(customer);
        address.setFirstName(addressDTO.getFirstName());
        address.setLastName(addressDTO.getLastName());
        address.setCompany(addressDTO.getCompany());
        address.setAddress1(addressDTO.getAddress1());
        address.setAddress2(addressDTO.getAddress2());
        address.setCity(addressDTO.getCity());
        address.setProvince(addressDTO.getProvince());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountryCode(addressDTO.getCountryCode());
        address.setPhone(addressDTO.getPhone());
        
        // 处理默认地址
        if (addressDTO.isDefaultShipping()) {
            customer.getAddresses().forEach(a -> a.setIsDefaultShipping(false));
            address.setIsDefaultShipping(true);
        }
        
        if (addressDTO.isDefaultBilling()) {
            customer.getAddresses().forEach(a -> a.setIsDefaultBilling(false));
            address.setIsDefaultBilling(true);
        }
        
        address.setMetadata(addressDTO.getMetadata());
        
        Address savedAddress = addressRepository.save(address);
        
        return mapToAddressDTO(savedAddress);
    }
    
    @Override
    public AddressDTO updateAddress(Long customerId, Long addressId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        // 确保地址属于该客户
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException("Address", "id", addressId);
        }
        
        if (addressDTO.getFirstName() != null) address.setFirstName(addressDTO.getFirstName());
        if (addressDTO.getLastName() != null) address.setLastName(addressDTO.getLastName());
        if (addressDTO.getCompany() != null) address.setCompany(addressDTO.getCompany());
        if (addressDTO.getAddress1() != null) address.setAddress1(addressDTO.getAddress1());
        if (addressDTO.getAddress2() != null) address.setAddress2(addressDTO.getAddress2());
        if (addressDTO.getCity() != null) address.setCity(addressDTO.getCity());
        if (addressDTO.getProvince() != null) address.setProvince(addressDTO.getProvince());
        if (addressDTO.getPostalCode() != null) address.setPostalCode(addressDTO.getPostalCode());
        if (addressDTO.getCountryCode() != null) address.setCountryCode(addressDTO.getCountryCode());
        if (addressDTO.getPhone() != null) address.setPhone(addressDTO.getPhone());
        
        // 处理默认地址
        if (addressDTO.isDefaultShipping() && !address.isIsDefaultShipping()) {
            customer.getAddresses().forEach(a -> a.setIsDefaultShipping(false));
            address.setIsDefaultShipping(true);
        }
        
        if (addressDTO.isDefaultBilling() && !address.isIsDefaultBilling()) {
            customer.getAddresses().forEach(a -> a.setIsDefaultBilling(false));
            address.setIsDefaultBilling(true);
        }
        
        address.setMetadata(addressDTO.getMetadata());
        
        Address updatedAddress = addressRepository.save(address);
        
        return mapToAddressDTO(updatedAddress);
    }
    
    @Override
    public void deleteAddress(Long customerId, Long addressId) {
        // Just check if customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        // 确保地址属于该客户
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException("Address", "id", addressId);
        }
        
        addressRepository.delete(address);
    }
    
    @Override
    public AddressDTO setDefaultAddress(Long customerId, Long addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        // 确保地址属于该客户
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException("Address", "id", addressId);
        }
        
        // 将所有地址设置为非默认（同时设置为默认送货和默认账单地址）
        customer.getAddresses().forEach(a -> {
            a.setIsDefaultShipping(false);
            a.setIsDefaultBilling(false);
        });
        
        // 将当前地址设置为默认
        address.setIsDefaultShipping(true);
        address.setIsDefaultBilling(true);
        
        Address updatedAddress = addressRepository.save(address);
        
        return mapToAddressDTO(updatedAddress);
    }
    
    @Override
    public CustomerDTO updateCustomerGroups(Long id, List<Long> groupIds) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        // 这里需要实现客户群组的更新逻辑
        // 假设有CustomerGroup实体和对应的Repository
        List<CustomerGroup> groups = groupIds.stream()
                .map(groupId -> customerGroupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("CustomerGroup", "id", groupId)))
                .collect(Collectors.toList());
        
        customer.setGroups(new HashSet<>(groups));
        customer.setUpdatedAt(LocalDateTime.now());
        
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }
    
    @Override
    public List<AddressDTO> getCustomerAddresses(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        return customer.getAddresses().stream()
                .map(this::mapToAddressDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AddressDTO getCustomerAddress(Long customerId, Long addressId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = customer.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        return mapToAddressDTO(address);
    }
    
    /**
     * 将Customer实体转换为CustomerDTO
     * @param customer Customer实体
     * @return CustomerDTO
     */
    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = CustomerDTO.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .avatarUrl(customer.getAvatarUrl())
                .hasAccount(customer.isHasAccount())
                .companyName(customer.getCompanyName())
                .defaultBillingAddressId(customer.getDefaultBillingAddressId())
                .defaultShippingAddressId(customer.getDefaultShippingAddressId())
                .metadata(customer.getMetadata())
                .createdBy(customer.getCreatedBy())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .deletedAt(customer.getDeletedAt())
                .build();
        
        // 加载地址
        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream()
                    .map(this::mapToAddressDTO)
                    .collect(Collectors.toList()));
        }
        
        // 加载客户组
        if (customer.getGroups() != null) {
            dto.setGroups(customer.getGroups().stream()
                    .map(this::mapToCustomerGroupDTO)
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
    
    /**
     * 将Address实体转换为AddressDTO
     * @param address Address实体
     * @return AddressDTO
     */
    private AddressDTO mapToAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .addressName(address.getAddressName())
                .company(address.getCompany())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .city(address.getCity())
                .countryCode(address.getCountryCode())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .phone(address.getPhone())
                .isDefaultShipping(address.isIsDefaultShipping())
                .isDefaultBilling(address.isIsDefaultBilling())
                .metadata(address.getMetadata())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
    
    // 将客户组实体映射为DTO
    private CustomerGroupDTO mapToCustomerGroupDTO(CustomerGroup group) {
        Map<String, Object> metadataMap = null;
        try {
            if (group.getMetadata() != null) {
                metadataMap = objectMapper.readValue(group.getMetadata(), new TypeReference<Map<String, Object>>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting metadata", e);
        }
        
        return CustomerGroupDTO.builder()
                .name(group.getName())
                .metadata(metadataMap)
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }

    // Add this method to map DTO to entity
    private Customer mapToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setUserId(dto.getUserId());
        customer.setEmail(dto.getEmail());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.setAvatarUrl(dto.getAvatarUrl());
        customer.setHasAccount(dto.isHasAccount());
        customer.setCompanyName(dto.getCompanyName());
        customer.setDefaultBillingAddressId(dto.getDefaultBillingAddressId());
        customer.setDefaultShippingAddressId(dto.getDefaultShippingAddressId());
        customer.setMetadata(dto.getMetadata());
        // Don't set createdAt, updatedAt here - they're set in the calling method
        return customer;
    }
} 