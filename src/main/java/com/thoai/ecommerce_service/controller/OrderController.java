package com.thoai.ecommerce_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.thoai.ecommerce_service.dto.request.OrderCreationRequest;
import com.thoai.ecommerce_service.dto.request.OrderUpdateRequest;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.response.OrderResponse;
import com.thoai.ecommerce_service.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    // Tạo đơn hàng - quyền ORDER_CREATE
    @PostMapping
    ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderCreationRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    // Lấy danh sách đơn hàng - quyền ORDER_READ_ALL (ADMIN và SHOP)
    @GetMapping
    ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrders())
                .build();
    }

    // Lấy đơn hàng theo ID đơn hàng - quyền ORDER_READ_ALL hoặc chủ đơn hàng
    @GetMapping("/{orderId}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    // Lấy đơn hàng của chính người dùng - quyền ORDER_READ
    @GetMapping("/myOrders")
    ApiResponse<List<OrderResponse>> getOrdersByUserId() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.myOrders())
                .build();
    }

    // Cập nhật đơn hàng - quyền ORDER_UPDATE (ADMIN và SHOP)
    @PutMapping("/{orderId}")
    ApiResponse<OrderResponse> updateOrder(
            @PathVariable("orderId") String orderId, @RequestBody OrderUpdateRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(orderId, request))
                .build();
    }

    // Xóa đơn hàng - quyền ORDER_DELETE (chỉ ADMIN)
    @DeleteMapping("/{orderId}")
    ApiResponse<String> deleteOrder(@PathVariable("orderId") String orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponse.<String>builder()
                .result("Đơn hàng đã được xóa thành công")
                .build();
    }

    // Hủy đơn hàng - quyền ORDER_CANCEL
    @PutMapping("/{orderId}/cancel")
    ApiResponse<OrderResponse> cancelOrder(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.cancelOrder(orderId))
                .build();
    }

    // Duyệt đơn hàng - quyền ORDER_APPROVE (ADMIN và SHOP)
    @PutMapping("/{orderId}/approve")
    ApiResponse<OrderResponse> approveOrder(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.approveOrder(orderId))
                .build();
    }
}
