package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")

public class Customer extends User {

    @Transient
    private Long userId;

    // 基本客户信息
    @Column(name = "has_account")
    private boolean hasAccount = true;

    @Column(name = "company_name")
    private String companyName;

    // 默认地址关联
    @Column(name = "default_billing_address_id")
    private Long defaultBillingAddressId;

    @Column(name = "default_shipping_address_id")
    private Long defaultShippingAddressId;

    // 元数据
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    // 审计字段
    @Column(name = "created_by")
    private String createdBy;

    // 客户地址列表
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "customer_group_customers",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<CustomerGroup> groups = new HashSet<>();

    public Customer() {
        // 确保客户角色
        this.getRoles().add("ROLE_CUSTOMER");
    }

    // 便捷方法获取用户ID
    public Long getUserId() {
        return this.getId(); // 因为继承了User，可以直接获取ID
    }
}