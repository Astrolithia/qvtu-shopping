package com.qvtu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    
    private Long id;
    private Long customerId;
    private String addressName;
    private String company;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String countryCode;
    private String province;
    private String postalCode;
    private String phone;
    private boolean isDefaultShipping;
    private boolean isDefaultBilling;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 