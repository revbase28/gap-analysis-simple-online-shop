package com.gap.analysis.be_simple_online_shop.model.item;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemRequest {

    @NotBlank(message = "Item name has to be filled")
    @Size(max = 255)
    private String itemName;

    @NotNull(message = "Stock has to be filled")
    @Positive
    private Long stock;

    @NotNull(message = "Price has to be filled")
    @Positive
    private Long price;

    @NotNull(message = "Is Available has to be filled")
    private Boolean isAvailable = true;

    private Date lastReStock = new Date();
}
