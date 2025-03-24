package com.qvtu.controller;

import com.qvtu.dto.ApiResponse;
import com.qvtu.dto.UserDTO;
import com.qvtu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
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
    
    @PutMapping("/users/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
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
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findById(id);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(userDTO)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id, @RequestBody UserDTO userDTO) {
        
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
} 