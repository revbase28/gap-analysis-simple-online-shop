package com.gap.analysis.be_simple_online_shop.repository;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByIsActive(Boolean isActive);

    Page<Customer> findByIsActiveTrue(Pageable pageable);

    @Query(value = "SELECT c FROM Customer c WHERE c.isActive = true AND c.customerName LIKE %:keyword%")
    Page<Customer> searchActiveCustomers(@Param("keyword") String keyword, Pageable pageable);
}
