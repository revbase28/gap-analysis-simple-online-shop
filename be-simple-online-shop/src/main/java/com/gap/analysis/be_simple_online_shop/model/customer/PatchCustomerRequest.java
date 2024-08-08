package com.gap.analysis.be_simple_online_shop.model.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchCustomerRequest {

    @Size(max = 255)
    private String customerName;

    @Size(max = 255)
    private String customerAddress;

    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "Phone only accept numeric")
    private String customerPhone;

    private Boolean isActive = true;

    private MultipartFile pic;

}
