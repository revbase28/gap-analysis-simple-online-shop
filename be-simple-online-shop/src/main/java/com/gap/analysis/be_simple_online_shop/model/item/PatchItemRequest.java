package com.gap.analysis.be_simple_online_shop.model.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchItemRequest {

    @Size(max = 255)
    private String itemName;

    @Positive
    private Long stock;

    @Positive
    private Long price;

    private Boolean isAvailable;
}
