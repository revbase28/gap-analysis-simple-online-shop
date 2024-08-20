package com.gap.analysis.be_simple_online_shop.repository;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @NotNull
    Page<Item> findAll(@NotNull Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.itemName LIKE %:keyword%")
    Page<Item> searchItem(@Param("keyword") String keyword, Pageable pageable);
}
