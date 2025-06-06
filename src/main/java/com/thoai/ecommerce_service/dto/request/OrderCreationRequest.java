package com.thoai.ecommerce_service.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
public class OrderCreationRequest {
    // userId có thể null, nếu null sẽ lấy từ người dùng đang đăng nhập
    String userId;

    @NotBlank(message = "Mã địa chỉ không được để trống")
    String addressId;

    @NotNull(message = "Tổng tiền không được để trống")
    BigDecimal totalAmount;
}
