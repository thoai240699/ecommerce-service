package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.thoai.ecommerce_service.dto.request.OrderCreationRequest;
import com.thoai.ecommerce_service.dto.request.OrderUpdateRequest;
import com.thoai.ecommerce_service.dto.response.OrderResponse;
import com.thoai.ecommerce_service.enums.OrderStatus;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.OrderMapper;
import com.thoai.ecommerce_service.repository.AddressRepository;
import com.thoai.ecommerce_service.repository.OrderRepository;
import com.thoai.ecommerce_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    AddressRepository addressRepository;

    // Tạo đơn hàng
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public OrderResponse createOrder(OrderCreationRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var address = addressRepository
                .findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        var order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // Lấy danh sách đơn hàng
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    // Lấy đơn hàng theo ID đơn hàng
    // Chỉ cho phép người dùng xem đơn hàng của chính mình hoặc người dùng có vai trò ADMIN
    @PreAuthorize("returnObject.username == authentication.name or hasRole('ADMIN' or hasRole('SELLER'))")
    public OrderResponse getOrderById(String orderId) {
        return orderRepository
                .findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    // Cập nhật đơn hàng
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public OrderResponse updateOrder(String orderId, OrderUpdateRequest request) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderMapper.updateOrder(order, request);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // Xóa đơn hàng
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    // Lấy danh sách đơn hàng theo userId
    public List<OrderResponse> myOrders() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        var order = orderRepository.findByUser_UserId(user.getUserId());
        return order.stream().map(orderMapper::toOrderResponse).toList();
    }
}
