package com.gap.analysis.be_simple_online_shop.model.order;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class AddOrderRequest {
    @NotNull(message = "Quantity has to be filled")
    @Positive
    @Min(1)
    private Long quantity;

    @NotNull(message = "Customer Id has to be filled")
    private Long customerId;

    @NotNull(message = "Item Id has to be filled")
    private Long itemId;
}
