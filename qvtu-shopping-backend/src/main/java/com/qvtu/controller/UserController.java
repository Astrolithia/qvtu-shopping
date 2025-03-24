package com.qvtu.controller;

import com.qvtu.dto.ApiResponse;
import com.qvtu.dto.UserDTO;
import com.qvtu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关的API")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取当前用户信息", description = "返回当前认证用户的详细信息")
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(Principal principal) {
        UserDTO userDTO = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User details retrieved successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "更新当前用户信息", description = "更新当前认证用户的信息")
    @PutMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDTO>> updateCurrentUser(
            @RequestBody UserDTO userDTO, Principal principal) {
        
        UserDTO currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserDTO updatedUser = userService.updateUser(currentUser.getId(), userDTO);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "修改密码", description = "修改当前用户的密码")
    @PutMapping("/users/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            Principal principal) {
        
        UserDTO currentUser = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        boolean success = userService.changePassword(currentUser.getId(), oldPassword, newPassword);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(success)
                .message(success ? "Password changed successfully" : "Failed to change password")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "获取所有用户", description = "管理员接口：获取所有用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        
        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(users)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "根据ID获取用户", description = "管理员接口：获取指定ID的用户信息")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "更新用户", description = "管理员接口：更新指定ID的用户信息")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "删除用户", description = "管理员接口：删除指定ID的用户")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
} 