package com.qvtu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qvtu.config.CustomLocalDateTimeSerializer;

import java.util.Map;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    
    private Long id;
    private String company;
    private String first_name; // 修改为下划线命名
    private String last_name;  // 修改为下划线命名
    private String address_1;  // 修改为下划线命名
    private String address_2;  // 修改为下划线命名
    private String city;
    private String country_code; // 修改为下划线命名
    private String province;
    private String postal_code; // 修改为下划线命名
    private String phone;
    private Map<String, Object> metadata;
    
    // 标记是否为默认地址
    private boolean is_default; // 新增，替代原来的defaultShipping和defaultBilling

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime created_at;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updated_at;
}