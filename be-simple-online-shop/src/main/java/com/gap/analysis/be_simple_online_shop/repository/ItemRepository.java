package com.gap.analysis.be_simple_online_shop.repository;

import com.gap.analysis.be_simple_online_shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
