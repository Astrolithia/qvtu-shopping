package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
public class Customer extends User {

    @Column(name = "has_account")
    private boolean hasAccount = true;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    // 默认地址关联（后续会添加地址实体）
    @Column(name = "billing_address_id")
    private Long billingAddressId;

    public Customer() {
        // 确保客户角色
        this.getRoles().add("ROLE_CUSTOMER");
    }
}