package com.gap.analysis.be_simple_online_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "order_code")
    private UUID orderCode = UUID.randomUUID();

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name="total_price", columnDefinition = "BIGINT UNSIGNED")
    private long totalPrice;

    @Column(columnDefinition = "MEDIUMINT UNSIGNED")
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "items_id", referencedColumnName = "items_id")
    private Item item;

}
