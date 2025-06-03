package com.thoai.ecommerce_service.dto.request;

import java.math.BigDecimal;

import com.thoai.ecommerce_service.constant.OrderStatus;

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
public class OrderUpdateRequest {
    String addressId;
    BigDecimal totalAmount;
    OrderStatus status;
}
