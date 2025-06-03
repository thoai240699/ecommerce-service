package com.thoai.ecommerce_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thoai.ecommerce_service.enums.OrderStatus;

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
public class OrderResponse {
    String orderId;
    String userId;
    String addressId;
    BigDecimal totalAmount;
    OrderStatus status;
    LocalDateTime createdAt;
}
