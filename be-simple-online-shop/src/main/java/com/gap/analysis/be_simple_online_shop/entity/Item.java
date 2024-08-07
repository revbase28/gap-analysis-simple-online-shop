package com.gap.analysis.be_simple_online_shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "items_id")
    private long itemId;

    @Column(name = "items_name")
    private String itemName;

    @Column(name = "items_code")
    private UUID itemCode = UUID.randomUUID();

    @Column(columnDefinition = "MEDIUMINT UNSIGNED")
    private long stock;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    private long price;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    private Date lastReStock;
}
