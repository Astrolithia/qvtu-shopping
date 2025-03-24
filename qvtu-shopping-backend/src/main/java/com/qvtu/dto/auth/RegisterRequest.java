package com.qvtu.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "电子邮件不能为空")
    @Email(message = "电子邮件格式不正确")
    private String email;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    private String password;
    
    @NotBlank(message = "名字不能为空")
    private String firstName;
    
    @NotBlank(message = "姓氏不能为空")
    private String lastName;
    
    private String phone;
} 