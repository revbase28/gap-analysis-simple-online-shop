package com.gap.analysis.be_simple_online_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_code")
    private UUID customerCode = UUID.randomUUID();

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_order_date")
    private Date lastOrderDate;

    private String pic;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
