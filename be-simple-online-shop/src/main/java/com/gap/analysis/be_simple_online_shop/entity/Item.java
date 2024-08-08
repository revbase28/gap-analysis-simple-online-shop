package com.gap.analysis.be_simple_online_shop.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    private long itemId;

    @Column(name = "items_name")
    private String itemName;

    @Column(name = "items_code")
    private UUID itemCode = UUID.randomUUID();

    @Column(columnDefinition = "MEDIUMINT UNSIGNED")
    private Long stock;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long price;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    private Date lastReStock;
}
