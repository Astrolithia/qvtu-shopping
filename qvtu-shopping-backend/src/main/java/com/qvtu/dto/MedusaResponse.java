package com.qvtu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedusaResponse<T> {
    private CustomerDTO customer;
    private List<CustomerDTO> customers;
    private AddressDTO address;
    private List<AddressDTO> addresses;
    private Integer count;
    private Integer offset;
    private Integer limit;
}