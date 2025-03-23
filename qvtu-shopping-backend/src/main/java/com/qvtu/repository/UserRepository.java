package com.qvtu.repository;

import com.qvtu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据电子邮件查找用户
     * @param email 电子邮件
     * @return 用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 判断电子邮件是否已存在
     * @param email 电子邮件
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据电子邮件和密码查找用户
     * @param email 电子邮件
     * @param password 密码
     * @return 用户
     */
    Optional<User> findByEmailAndPassword(String email, String password);
} 