package com.qvtu.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerGroupRequest {
    private List<Long> groups;
} 