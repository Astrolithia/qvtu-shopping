package com.qvtu.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qvtu.dto.AddressDTO;
import com.qvtu.dto.CustomerDTO;
import com.qvtu.dto.CustomerGroupDTO;
import com.qvtu.dto.UserDTO;
import com.qvtu.exception.EmailAlreadyExistsException;
import com.qvtu.exception.ResourceNotFoundException;
import com.qvtu.model.Address;
import com.qvtu.model.Customer;
import com.qvtu.model.CustomerGroup;
import com.qvtu.repository.AddressRepository;
import com.qvtu.repository.CustomerRepository;
import com.qvtu.repository.CustomerGroupRepository;
import com.qvtu.service.CustomerService;
import com.qvtu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
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
        // 调用双参数版本，传入null作为密码，这会触发随机密码生成
        return createCustomer(customerDTO, null);
    }
    
    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO, String password) {
        // 检查邮箱是否已存在
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new EmailAlreadyExistsException(customerDTO.getEmail());
        }
        
        // 创建UserDTO对象
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(customerDTO.getEmail());
        userDTO.setFirstName(customerDTO.getFirst_name());
        userDTO.setLastName(customerDTO.getLast_name());
        userDTO.setPhone(customerDTO.getPhone());
        userDTO.setActive(true);
        
        // 设置密码
        if (password == null || password.isEmpty()) {
            // 生成随机密码
            password = generateRandomPassword();
        }
        
        // 1. 先创建User实体
        UserDTO createdUserDTO = userService.createUser(userDTO, password);
        Long userId = createdUserDTO.getId();
        
        try {
            // 2. 使用原生SQL插入Customer记录 - 只使用确定存在的列
            int result = entityManager.createNativeQuery(
                "INSERT INTO customers (id, has_account, company_name) VALUES (?, ?, ?)")
                .setParameter(1, userId)
                .setParameter(2, true)
                .setParameter(3, customerDTO.getCompany_name())
                .executeUpdate();
            
            if (result != 1) {
                throw new RuntimeException("Failed to insert customer record");
            }
            
            // 3. 直接从数据库查询整合后的Customer实体
            entityManager.flush();
            entityManager.clear(); // 清除持久化上下文
            
            Customer savedCustomer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found after creation"));
            
            return mapToDTO(savedCustomer);
        } catch (Exception e) {
            // 详细记录错误
            e.printStackTrace();
            throw new RuntimeException("Error creating customer: " + e.getMessage(), e);
        }
    }
    
    private String generateRandomPassword() {
        int length = 12;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
    
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirst_name());
        customer.setLastName(customerDTO.getLast_name());
        customer.setPhone(customerDTO.getPhone());
        customer.setAvatarUrl(customerDTO.getAvatar_url());
        
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
        try {
            // 检查客户是否存在
            if (!customerRepository.existsById(customerId)) {
                throw new ResourceNotFoundException("Customer", "id", customerId);
            }
            
            // 直接从AddressRepository查询地址
            List<Address> addresses = addressRepository.findByCustomerId(customerId);
            
            // 转换为DTO并返回
            return addresses.stream()
                    .map(this::mapToAddressDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving customer addresses: " + 
                    (e.getMessage() != null ? e.getMessage() : "Database error fetching addresses"), e);
        }
    }
    
    @Override
    public AddressDTO addAddress(Long customerId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        
        Address address = new Address();
        address.setCustomer(customer);
        address.setFirstName(addressDTO.getFirst_name());
        address.setLastName(addressDTO.getLast_name());
        address.setCompany(addressDTO.getCompany());
        address.setAddress1(addressDTO.getAddress_1());
        address.setAddress2(addressDTO.getAddress_2());
        address.setCity(addressDTO.getCity());
        address.setProvince(addressDTO.getProvince());
        address.setPostalCode(addressDTO.getPostal_code());
        address.setCountryCode(addressDTO.getCountry_code());
        address.setPhone(addressDTO.getPhone());
        
        // 处理默认地址
        if (addressDTO.is_default()) {
            customer.getAddresses().forEach(a -> a.setIsDefaultShipping(false));
            address.setIsDefaultShipping(true);
            
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
        
        // 更新地址属性
        if (addressDTO.getFirst_name() != null) address.setFirstName(addressDTO.getFirst_name());
        if (addressDTO.getLast_name() != null) address.setLastName(addressDTO.getLast_name());
        if (addressDTO.getCompany() != null) address.setCompany(addressDTO.getCompany());
        if (addressDTO.getAddress_1() != null) address.setAddress1(addressDTO.getAddress_1());
        if (addressDTO.getAddress_2() != null) address.setAddress2(addressDTO.getAddress_2());
        if (addressDTO.getCity() != null) address.setCity(addressDTO.getCity());
        if (addressDTO.getProvince() != null) address.setProvince(addressDTO.getProvince());
        if (addressDTO.getPostal_code() != null) address.setPostalCode(addressDTO.getPostal_code());
        if (addressDTO.getCountry_code() != null) address.setCountryCode(addressDTO.getCountry_code());
        if (addressDTO.getPhone() != null) address.setPhone(addressDTO.getPhone());
        
        // 处理默认设置
        if (addressDTO.is_default()) {
            // 先取消其他地址的默认配送设置
            customer.getAddresses().stream()
                .filter(a -> !a.getId().equals(addressId))
                .forEach(a -> a.setIsDefaultShipping(false));
            address.setIsDefaultShipping(true);
            
            // 先取消其他地址的默认账单设置
            customer.getAddresses().stream()
                .filter(a -> !a.getId().equals(addressId))
                .forEach(a -> a.setIsDefaultBilling(false));
            address.setIsDefaultBilling(true);
        }
        
        // 更新元数据
        if (addressDTO.getMetadata() != null) {
            address.setMetadata(addressDTO.getMetadata());
        }
        
        // 保存更新后的地址
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
        if (customer == null) {
            return null;
        }
        
        // 先初始化懒加载集合，避免在流处理中懒加载导致的问题
        Hibernate.initialize(customer.getAddresses());
        Hibernate.initialize(customer.getGroups());
        
        // 创建新集合来避免并发修改异常
        Set<AddressDTO> addressDTOs = new HashSet<>();
        if (customer.getAddresses() != null) {
            // 创建一个新的ArrayList作为安全副本
            List<Address> addressesCopy = new ArrayList<>(customer.getAddresses());
            addressDTOs = addressesCopy.stream()
                    .map(address -> mapToAddressDTO(address))
                    .collect(Collectors.toSet());
        }
        
        Set<CustomerGroupDTO> groupDTOs = new HashSet<>();
        if (customer.getGroups() != null) {
            // 创建一个新的ArrayList作为安全副本
            List<CustomerGroup> groupsCopy = new ArrayList<>(customer.getGroups());
            groupDTOs = groupsCopy.stream()
                    .map(group -> mapToCustomerGroupDTO(group))
                    .collect(Collectors.toSet());
        }
        
        // Convert only addresses to List, keep groups as Set
        List<AddressDTO> addressDTOList = new ArrayList<>(addressDTOs);
        
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .first_name(customer.getFirstName())
                .last_name(customer.getLastName())
                .phone(customer.getPhone())
                .has_account(customer.isHasAccount())
                .billing_address_id(customer.getDefaultBillingAddressId())
                .shipping_address_id(customer.getDefaultShippingAddressId())
                .shipping_addresses(addressDTOList)
                .metadata(customer.getMetadata())
                .created_at(customer.getCreatedAt())
                .updated_at(customer.getUpdatedAt())
                .deleted_at(customer.getDeletedAt())
                .groups(groupDTOs)
                .build();
        
        return customerDTO;
    }
    
    /**
     * 将Address实体转换为AddressDTO
     * @param address Address实体
     * @return AddressDTO
     */
    private AddressDTO mapToAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .company(address.getCompany())
                .first_name(address.getFirstName())
                .last_name(address.getLastName())
                .address_1(address.getAddress1())
                .address_2(address.getAddress2())
                .city(address.getCity())
                .country_code(address.getCountryCode())
                .province(address.getProvince())
                .postal_code(address.getPostalCode())
                .phone(address.getPhone())
                .is_default(address.isDefaultShipping() || address.isDefaultBilling())
                .metadata(address.getMetadata())
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
                .id(group.getId())
                .name(group.getName())
                .metadata(metadataMap)
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }

    // Add this method to map DTO to entity
    private Customer mapToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setUserId(dto.getId());
        customer.setEmail(dto.getEmail());
        customer.setFirstName(dto.getFirst_name());
        customer.setLastName(dto.getLast_name());
        customer.setPhone(dto.getPhone());
        customer.setAvatarUrl(dto.getAvatar_url());
        customer.setHasAccount(dto.isHas_account());
        customer.setCompanyName(dto.getCompany_name());
        customer.setDefaultBillingAddressId(dto.getBilling_address_id());
        customer.setDefaultShippingAddressId(dto.getShipping_address_id());
        customer.setMetadata(dto.getMetadata());
        // Don't set createdAt, updatedAt here - they're set in the calling method
        return customer;
    }
} 