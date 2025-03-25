package com.qvtu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qvtu.config.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    
    private Long id;
    private String email;
    private String first_name; // 修改为下划线命名
    private String last_name;  // 修改为下划线命名
    private String phone;
    @JsonIgnore
    private String password; // 仅用于创建请求，不会返回
    private boolean has_account; // 修改为下划线命名
    
    // 地址相关
    private Long billing_address_id; // 修改为下划线命名
    private Long shipping_address_id; // 修改为下划线命名
    private List<AddressDTO> shipping_addresses; // 修改为下划线命名
    
    // 元数据
    private Map<String, Object> metadata;
    
    // 客户分组
    private Set<CustomerGroupDTO> groups;
    
    // 审计字段
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime created_at; // 修改为下划线命名
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updated_at; // 修改为下划线命名
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime deleted_at; // 修改为下划线命名

    // 添加缺失的字段
    private String company_name;
    private String avatar_url;
}