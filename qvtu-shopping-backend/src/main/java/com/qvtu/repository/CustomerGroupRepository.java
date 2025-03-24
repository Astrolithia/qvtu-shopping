package com.qvtu.repository;

import com.qvtu.model.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Long> {
    // You can add custom query methods here as needed
} 