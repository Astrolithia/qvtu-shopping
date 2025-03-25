package com.qvtu.controller;

import com.qvtu.dto.*;
import com.qvtu.service.CustomerService;
import com.qvtu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.ArrayList;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    private final UserService userService;
    
    // 客户个人操作 - 需要认证
    @GetMapping("/customers/me")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCurrentCustomer(Principal principal) {
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer details retrieved successfully")
                .data(customerDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/customers/me")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCurrentCustomer(
            @RequestBody CustomerDTO customerDTO, Principal principal) {
        
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO currentCustomer = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        CustomerDTO updatedCustomer = customerService.updateCustomer(currentCustomer.getId(), customerDTO);
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer updated successfully")
                .data(updatedCustomer)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    // 地址相关操作
    @GetMapping("/customers/me/addresses")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getCurrentCustomerAddresses(Principal principal) {
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        List<AddressDTO> addresses = customerService.getAddressesByCustomerId(customerDTO.getId());
        
        ApiResponse<List<AddressDTO>> response = ApiResponse.<List<AddressDTO>>builder()
                .success(true)
                .message("Addresses retrieved successfully")
                .data(addresses)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/customers/me/addresses")
    public ResponseEntity<ApiResponse<AddressDTO>> addAddress(
            @RequestBody AddressDTO addressDTO, Principal principal) {
        
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        AddressDTO savedAddress = customerService.addAddress(customerDTO.getId(), addressDTO);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Address added successfully")
                .data(savedAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/customers/me/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressDTO>> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressDTO addressDTO,
            Principal principal) {
        
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        AddressDTO updatedAddress = customerService.updateAddress(customerDTO.getId(), addressId, addressDTO);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Address updated successfully")
                .data(updatedAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/customers/me/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable Long addressId, Principal principal) {
        
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customerService.deleteAddress(customerDTO.getId(), addressId);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Address deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/customers/me/addresses/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressDTO>> setDefaultAddress(
            @PathVariable Long addressId, Principal principal) {
        
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CustomerDTO customerDTO = customerService.findByUserId(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        AddressDTO defaultAddress = customerService.setDefaultAddress(customerDTO.getId(), addressId);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Default address set successfully")
                .data(defaultAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    // 管理员操作 - 需要ADMIN角色
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers")
    public ResponseEntity<ApiResponse<Page<CustomerDTO>>> getAllCustomers(Pageable pageable) {
        Page<CustomerDTO> customers = customerService.findAll(pageable);
        
        ApiResponse<Page<CustomerDTO>> response = ApiResponse.<Page<CustomerDTO>>builder()
                .success(true)
                .message("Customers retrieved successfully")
                .data(customers)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/search")
    public ResponseEntity<ApiResponse<Page<CustomerDTO>>> searchCustomers(
            @RequestParam String query, Pageable pageable) {
        
        Page<CustomerDTO> customers = customerService.search(query, pageable);
        
        ApiResponse<Page<CustomerDTO>> response = ApiResponse.<Page<CustomerDTO>>builder()
                .success(true)
                .message("Customers retrieved successfully")
                .data(customers)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO customerDTO = customerService.findById(id);
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer retrieved successfully")
                .data(customerDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers")
    @Operation(summary = "创建客户", description = "管理员创建新客户")
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(
            @RequestBody @Valid CustomerDTO customerDTO,
            @RequestParam(required = false) String password) {
        
        // 如果未提供密码，生成一个随机密码
        String finalPassword = password;
        if (finalPassword == null || finalPassword.isEmpty()) {
            // 生成12位随机密码
            finalPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        }
        
        // 修改为调用createCustomer方法
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, finalPassword);
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer created successfully")
                .data(createdCustomer)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer updated successfully")
                .data(updatedCustomer)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/customers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Customer deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/customers/{id}/addresses")
    @Operation(summary = "获取客户地址列表", description = "获取特定客户的所有地址")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getCustomerAddresses(@PathVariable Long id) {
        try {
            // 获取客户地址列表，使用更可靠的方法
            List<AddressDTO> addresses = customerService.getAddressesByCustomerId(id);
            
            // 确保返回空列表而不是null
            if (addresses == null) {
                addresses = new ArrayList<>();
            }
            
            ApiResponse<List<AddressDTO>> response = ApiResponse.<List<AddressDTO>>builder()
                    .success(true)
                    .message("Customer addresses retrieved successfully")
                    .data(addresses)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 记录详细错误
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            
            ApiResponse<List<AddressDTO>> errorResponse = ApiResponse.<List<AddressDTO>>builder()
                    .success(false)
                    .message("Failed to retrieve customer addresses: " + errorMessage)
                    .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/admin/customers/{id}/addresses/{addressId}")
    @Operation(summary = "获取客户特定地址", description = "获取客户的特定地址")
    public ResponseEntity<ApiResponse<AddressDTO>> getCustomerAddress(
            @PathVariable Long id,
            @PathVariable Long addressId) {
        AddressDTO address = customerService.getCustomerAddress(id, addressId);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Address retrieved successfully")
                .data(address)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/admin/customers/{id}/customer-groups")
    @Operation(summary = "管理客户群组", description = "将客户添加到指定客户群组")
    public ResponseEntity<ApiResponse<CustomerDTO>> manageCustomerGroups(
            @PathVariable Long id,
            @RequestBody CustomerGroupRequest request) {
        CustomerDTO updatedCustomer = customerService.updateCustomerGroups(id, request.getGroups());
        
        ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                .success(true)
                .message("Customer groups updated successfully")
                .data(updatedCustomer)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/customers/{id}/addresses")
    @Operation(summary = "管理员添加客户地址", description = "管理员为指定客户添加新地址")
    public ResponseEntity<ApiResponse<AddressDTO>> addCustomerAddress(
            @PathVariable Long id,
            @RequestBody AddressDTO addressDTO) {
        
        AddressDTO savedAddress = customerService.addAddress(id, addressDTO);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Address added successfully")
                .data(savedAddress)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/customers/{id}/addresses/{addressId}")
    @Operation(summary = "管理员更新客户地址", description = "管理员为指定客户更新地址信息")
    public ResponseEntity<ApiResponse<AddressDTO>> updateCustomerAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @RequestBody AddressDTO addressDTO) {
        
        AddressDTO updatedAddress = customerService.updateAddress(id, addressId, addressDTO);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Address updated successfully")
                .data(updatedAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/customers/{id}/addresses/{addressId}")
    @Operation(summary = "管理员删除客户地址", description = "管理员删除指定客户的地址")
    public ResponseEntity<ApiResponse<Void>> deleteCustomerAddress(
            @PathVariable Long id,
            @PathVariable Long addressId) {
        
        customerService.deleteAddress(id, addressId);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Address deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/customers/{id}/addresses/{addressId}/default")
    @Operation(summary = "管理员设置客户默认地址", description = "管理员为指定客户设置默认地址")
    public ResponseEntity<ApiResponse<AddressDTO>> setCustomerDefaultAddress(
            @PathVariable Long id,
            @PathVariable Long addressId) {
        
        AddressDTO defaultAddress = customerService.setDefaultAddress(id, addressId);
        
        ApiResponse<AddressDTO> response = ApiResponse.<AddressDTO>builder()
                .success(true)
                .message("Default address set successfully")
                .data(defaultAddress)
                .build();
        
        return ResponseEntity.ok(response);
    }
} 