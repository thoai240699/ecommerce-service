package com.thoai.ecommerce_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressCreateRequest {
    @NotBlank(message = "Mã người dùng không được để trống")
    String userId;
    String streetAddress;
    String ward;
    String district;
    String city;
}
