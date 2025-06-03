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

    // Tạo đơn hàng
    @PostMapping
    ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderCreationRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    // Lấy danh sách đơn hàng
    @GetMapping
    ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrders())
                .build();
    }

    // Lấy đơn hàng theo ID đơn hàng
    @GetMapping("/{orderId}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    // Lấy đơn hàng theo của chính người dùng
    @GetMapping("/myOrders")
    ApiResponse<List<OrderResponse>> getOrdersByUserId() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.myOrders())
                .build();
    }

    // Cập nhật đơn hàng
    @PutMapping("/{orderId}")
    ApiResponse<OrderResponse> updateOrder(
            @PathVariable("orderId") String orderId, @RequestBody OrderUpdateRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(orderId, request))
                .build();
    }

    // Xóa đơn hàng
    @DeleteMapping("/{orderId}")
    ApiResponse<String> deleteOrder(@PathVariable("orderId") String orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponse.<String>builder()
                .result("Đơn hàng đã được xóa thành công")
                .build();
    }
}
