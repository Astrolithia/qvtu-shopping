package com.qvtu.controller;

import com.qvtu.dto.*;
import com.qvtu.service.CustomerService;
import com.qvtu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    // ========== 管理员API ==========
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers")
    @Operation(summary = "获取客户列表", description = "管理员获取所有客户")
    public ResponseEntity<MedusaResponse<List<CustomerDTO>>> getCustomers(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) List<String> expand,
            @RequestParam(required = false) List<String> fields) {
        
        Pageable pageable = PageRequest.of(offset / limit, limit);
        
        Page<CustomerDTO> customersPage;
        if (q != null && !q.isEmpty()) {
            customersPage = customerService.search(q, pageable);
        } else {
            customersPage = customerService.findAll(pageable);
        }
        
        MedusaResponse<List<CustomerDTO>> response = MedusaResponse.<List<CustomerDTO>>builder()
                .customers(customersPage.getContent())
                .count((int) customersPage.getTotalElements())
                .offset(offset)
                .limit(limit)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers")
    @Operation(summary = "创建客户", description = "管理员创建新客户")
    public ResponseEntity<MedusaResponse<CustomerDTO>> createCustomer(
            @RequestBody @Valid CustomerDTO customerDTO) {
        
        // 处理密码，如果未提供则生成随机密码
        String password = customerDTO.getPassword();
        if (password == null || password.isEmpty()) {
            password = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
            customerDTO.setPassword(password);
        }
        
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, password);
        
        MedusaResponse<CustomerDTO> response = MedusaResponse.<CustomerDTO>builder()
                .customer(createdCustomer)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/{id}")
    @Operation(summary = "获取单个客户", description = "管理员获取指定客户详情")
    public ResponseEntity<MedusaResponse<CustomerDTO>> getCustomer(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        
        MedusaResponse<CustomerDTO> response = MedusaResponse.<CustomerDTO>builder()
                .customer(customer)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}")
    @Operation(summary = "更新客户", description = "管理员更新客户信息")
    public ResponseEntity<MedusaResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id, 
            @RequestBody AdminCustomerUpdateDTO updateDTO) {
        
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(updateDTO.getEmail());
        customerDTO.setFirst_name(updateDTO.getFirst_name());
        customerDTO.setLast_name(updateDTO.getLast_name());
        customerDTO.setPhone(updateDTO.getPhone());
        customerDTO.setMetadata(updateDTO.getMetadata());
        
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        
        MedusaResponse<CustomerDTO> response = MedusaResponse.<CustomerDTO>builder()
                .customer(updatedCustomer)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/customers/{id}")
    @Operation(summary = "删除客户", description = "管理员删除客户")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 客户地址管理
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/{id}/addresses")
    @Operation(summary = "获取客户地址列表", description = "获取特定客户的所有地址")
    public ResponseEntity<MedusaResponse<List<AddressDTO>>> getCustomerAddresses(@PathVariable Long id) {
        List<AddressDTO> addresses = customerService.getAddressesByCustomerId(id);
        
        MedusaResponse<List<AddressDTO>> response = MedusaResponse.<List<AddressDTO>>builder()
                .addresses(addresses)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}/addresses")
    @Operation(summary = "添加客户地址", description = "为客户添加新地址")
    public ResponseEntity<MedusaResponse<AddressDTO>> addCustomerAddress(
            @PathVariable Long id,
            @RequestBody AddressDTO addressDTO) {
        
        AddressDTO savedAddress = customerService.addAddress(id, addressDTO);
        
        MedusaResponse<AddressDTO> response = MedusaResponse.<AddressDTO>builder()
                .address(savedAddress)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/{id}/addresses/{address_id}")
    @Operation(summary = "获取客户特定地址", description = "获取客户的特定地址")
    public ResponseEntity<MedusaResponse<AddressDTO>> getCustomerAddress(
            @PathVariable Long id,
            @PathVariable("address_id") Long addressId) {
        
        AddressDTO address = customerService.getCustomerAddress(id, addressId);
        
        MedusaResponse<AddressDTO> response = MedusaResponse.<AddressDTO>builder()
                .address(address)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}/addresses/{address_id}")
    @Operation(summary = "更新客户地址", description = "更新客户的特定地址")
    public ResponseEntity<MedusaResponse<AddressDTO>> updateCustomerAddress(
            @PathVariable Long id,
            @PathVariable("address_id") Long addressId,
            @RequestBody AddressDTO addressDTO) {
        
        AddressDTO updatedAddress = customerService.updateAddress(id, addressId, addressDTO);
        
        MedusaResponse<AddressDTO> response = MedusaResponse.<AddressDTO>builder()
                .address(updatedAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/customers/{id}/addresses/{address_id}")
    @Operation(summary = "删除客户地址", description = "删除客户的特定地址")
    public ResponseEntity<Void> deleteCustomerAddress(
            @PathVariable Long id,
            @PathVariable("address_id") Long addressId) {
        
        customerService.deleteAddress(id, addressId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}/customer-groups")
    @Operation(summary = "管理客户群组", description = "为客户添加客户群组")
    public ResponseEntity<MedusaResponse<CustomerDTO>> addCustomerToGroups(
            @PathVariable Long id,
            @RequestBody CustomerGroupRequest request) {
        
        CustomerDTO updatedCustomer = customerService.updateCustomerGroups(id, request.getGroups());
        
        MedusaResponse<CustomerDTO> response = MedusaResponse.<CustomerDTO>builder()
                .customer(updatedCustomer)
                .build();
        
        return ResponseEntity.ok(response);
    }
} 