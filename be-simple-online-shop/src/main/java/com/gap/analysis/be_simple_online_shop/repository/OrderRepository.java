package com.gap.analysis.be_simple_online_shop.repository;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.entity.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional
    void deleteByCustomer(Customer customer);

    @Transactional
    void deleteByItem(Item item);

    @NotNull
    Page<Order> findAll(@NotNull Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.customer.customerName LIKE %:keyword% OR o.item.itemName LIKE %:keyword%")
    Page<Order> searchOrder(@Param("keyword") String keyword, Pageable pageable);
}
