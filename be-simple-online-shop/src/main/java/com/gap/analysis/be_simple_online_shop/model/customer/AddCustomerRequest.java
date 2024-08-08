package com.gap.analysis.be_simple_online_shop.model.customer;

import jakarta.validation.constraints.NotBlank;
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
public class AddCustomerRequest {

    @NotBlank(message = "Customer name has to be filled")
    @Size(max = 255)
    private String customerName;

    @NotBlank(message = "Customer address has to be filled")
    @Size(max = 255)
    private String customerAddress;

    @NotBlank(message = "Customer phone has to be filled")
    @Size(max = 20)
    private String customerPhone;

    private MultipartFile pic;

}
