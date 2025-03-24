package com.qvtu.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "company")
    private String company;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address_1", nullable = false)
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(nullable = false)
    private String city;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column
    private String province;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column
    private String phone;

    @Column(name = "is_default_shipping")
    private boolean defaultShipping;

    @Column(name = "is_default_billing")
    private boolean defaultBilling;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata = new HashMap<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public boolean isIsDefaultShipping() {
        return this.defaultShipping;
    }

    public void setIsDefaultShipping(boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public boolean isIsDefaultBilling() {
        return this.defaultBilling;
    }

    public void setIsDefaultBilling(boolean defaultBilling) {
        this.defaultBilling = defaultBilling;
    }
}
