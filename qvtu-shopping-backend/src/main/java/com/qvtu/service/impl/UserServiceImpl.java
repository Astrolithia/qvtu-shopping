package com.qvtu.service.impl;

import com.qvtu.dto.UserDTO;
import com.qvtu.dto.auth.AuthResponse;
import com.qvtu.dto.auth.LoginRequest;
import com.qvtu.dto.auth.RegisterRequest;
import com.qvtu.exception.AuthenticationException;
import com.qvtu.exception.EmailAlreadyExistsException;
import com.qvtu.exception.ResourceNotFoundException;
import com.qvtu.model.User;
import com.qvtu.repository.UserRepository;
import com.qvtu.service.UserService;
import com.qvtu.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // 根据邮箱查找用户
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));
        
        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getEmail());
        
        // 转换为UserDTO
        UserDTO userDTO = mapToDTO(user);
        
        // 返回认证响应
        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .message("Login successful")
                .build();
    }
    
    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(registerRequest.getEmail());
        }
        
        // 创建新用户
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.getRoles().add("ROLE_CUSTOMER");
        user.setActive(true);
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 生成JWT令牌
        String token = jwtUtil.generateToken(savedUser.getEmail());
        
        // 转换为UserDTO
        UserDTO userDTO = mapToDTO(savedUser);
        
        // 返回认证响应
        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .message("Registration successful")
                .build();
    }
    
    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(user);
    }
    
    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO);
    }
    
    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // 更新用户信息
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setAvatarUrl(userDTO.getAvatarUrl());
        user.setActive(userDTO.isActive());
        
        // 保存用户
        User updatedUser = userRepository.save(user);
        
        return mapToDTO(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        userRepository.delete(user);
    }
    
    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        return true;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 将User实体转换为UserDTO
     * @param user User实体
     * @return UserDTO
     */
    private UserDTO mapToDTO(User user) {
        // 假设我们有某种方式将JSON字符串转换为Map
        Map<String, Object> metadataMap = convertMetadataToMap(user.getMetadata());
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRoles().stream().findFirst().orElse(""))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .active(user.isActive())
                .metadata(metadataMap)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
    
    // 将JSON字符串转换为Map的辅助方法
    private Map<String, Object> convertMetadataToMap(String metadata) {
        // 这里需要实现JSON转Map的逻辑
        // 如果使用Jackson，可以使用ObjectMapper
        // 为简化，这里返回null
        return null;
    }
} 