package com.thoai.ecommerce_service.service;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    AddressRepository addressRepository;

    // Tạo đơn hàng - quyền ORDER_CREATE và chỉ cho phép tạo đơn hàng cho chính mình
    // (trừ ADMIN)
    @PreAuthorize("hasAuthority('ORDER_CREATE') and (hasAuthority('SYSTEM_ADMIN') or @userRepository.findById(#request.userId).get().username == authentication.name)")
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

    // Lấy danh sách tất cả đơn hàng - quyền ORDER_READ_ALL (ADMIN và SHOP)
    @PreAuthorize("hasAuthority('ORDER_READ_ALL')")
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    // Lấy đơn hàng theo ID - quyền ORDER_READ_ALL hoặc chính chủ đơn hàng
    @PreAuthorize("hasAuthority('ORDER_READ_ALL') or @orderRepository.findById(#orderId).get().user.username == authentication.name")
    public OrderResponse getOrderById(String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    // Cập nhật đơn hàng - quyền ORDER_UPDATE (ADMIN và SHOP)
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public OrderResponse updateOrder(String orderId, OrderUpdateRequest request) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orderMapper.updateOrder(order, request);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // Xóa đơn hàng - chỉ ADMIN có quyền ORDER_DELETE
    @PreAuthorize("hasAuthority('ORDER_DELETE')")
    public void deleteOrder(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    // Lấy đơn hàng của chính mình - quyền ORDER_READ
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public List<OrderResponse> myOrders() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        var orders = orderRepository.findByUser_UserId(user.getUserId());
        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

    // Hủy đơn hàng - quyền ORDER_CANCEL và chỉ chủ đơn hàng hoặc ADMIN
    @PreAuthorize("hasAuthority('ORDER_CANCEL') and (hasAuthority('SYSTEM_ADMIN') or @orderRepository.findById(#orderId).get().user.username == authentication.name)")
    public OrderResponse cancelOrder(String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Chỉ có thể hủy đơn hàng ở trạng thái PENDING hoặc PROCESSING
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    // Duyệt đơn hàng - quyền ORDER_APPROVE (ADMIN và SHOP)
    @PreAuthorize("hasAuthority('ORDER_APPROVE')")
    public OrderResponse approveOrder(String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        order.setStatus(OrderStatus.PROCESSING);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
