package com.gap.analysis.be_simple_online_shop.model.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchOrderRequest {

    @NotNull(message = "Quantity field has to be filled")
    @Positive
    @Min(1)
    private Long quantity;

    private Long itemId;
}
