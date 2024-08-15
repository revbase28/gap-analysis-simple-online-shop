package com.gap.analysis.be_simple_online_shop.model.order;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReport {

    private long orderId;

    private UUID orderCode = UUID.randomUUID();

    private Date orderDate;

    private long totalPrice;

    private long quantity;

    private String customerName;

    private String itemName;
}
