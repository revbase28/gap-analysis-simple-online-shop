package com.gap.analysis.be_simple_online_shop.repository;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByIsActive(Boolean isActive);
}
