package com.qvtu.service;

import com.qvtu.dto.UserDTO;
import com.qvtu.dto.auth.AuthResponse;
import com.qvtu.dto.auth.LoginRequest;
import com.qvtu.dto.auth.RegisterRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 认证响应
     */
    AuthResponse login(LoginRequest loginRequest);
    
    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 认证响应
     */
    AuthResponse register(RegisterRequest registerRequest);
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户DTO
     */
    UserDTO findById(Long id);
    
    /**
     * 根据电子邮件查询用户
     * @param email 电子邮件
     * @return 用户DTO
     */
    Optional<UserDTO> findByEmail(String email);
    
    /**
     * 查询所有用户
     * @return 用户DTO列表
     */
    List<UserDTO> findAll();
    
    /**
     * 更新用户信息
     * @param id 用户ID
     * @param userDTO 用户DTO
     * @return 更新后的用户DTO
     */
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);
    
    /**
     * 更改用户密码
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更改成功
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 检查电子邮件是否已存在
     * @param email 电子邮件
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 设置用户角色
     * @param id 用户ID
     * @param role 角色名称
     * @return 更新后的用户DTO
     */
    UserDTO setUserRole(Long id, String role);
    
    /**
     * 创建用户
     * @param userDTO 用户DTO
     * @param password 密码
     * @return 创建后的用户DTO
     */
    UserDTO createUser(UserDTO userDTO, String password);
} 