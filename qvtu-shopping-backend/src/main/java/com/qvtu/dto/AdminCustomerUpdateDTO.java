package com.qvtu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCustomerUpdateDTO {
    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String password;
    private Map<String, Object> metadata;
}