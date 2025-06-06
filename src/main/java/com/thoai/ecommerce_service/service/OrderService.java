package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.thoai.ecommerce_service.dto.request.OrderCreationRequest;
import com.thoai.ecommerce_service.dto.request.OrderUpdateRequest;
import com.thoai.ecommerce_service.dto.response.OrderResponse;
import com.thoai.ecommerce_service.entity.Order;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.enums.OrderStatus;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.OrderMapper;
import com.thoai.ecommerce_service.repository.AddressRepository;
import com.thoai.ecommerce_service.repository.OrderRepository;
import com.thoai.ecommerce_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    AddressRepository addressRepository;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    // Tạo đơn hàng - quyền ORDER_CREATE
    // USER chỉ có thể tạo đơn hàng cho chính mình
    // SHOP và ADMIN có thể tạo đơn hàng cho bất kỳ user nào
    @PreAuthorize("hasAuthority('ORDER_CREATE') and " +
            "(hasAnyAuthority('SHOP', 'ADMIN') or " +
            "(#request.userId == null or @userRepository.findById(#request.userId).get().username == authentication.name))")
    public OrderResponse createOrder(OrderCreationRequest request) {
        String userId;
        if (request.getUserId() == null) {
            // Nếu không có userId, lấy từ người dùng đang đăng nhập
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND))
                    .getUserId();
        } else {
            // Nếu có userId, kiểm tra quyền và lấy thông tin user
            userId = request.getUserId();
            if (!userRepository.existsById(userId)) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var address = addressRepository
                .findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        // Kiểm tra địa chỉ có thuộc về user không
        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

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
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    // Cập nhật đơn hàng - quyền ORDER_UPDATE (ADMIN và SHOP)
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public OrderResponse updateOrder(String orderId, OrderUpdateRequest request) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        var orders = orderRepository.findByUser_UserId(user.getUserId());
        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

    // Hủy đơn hàng - quyền ORDER_CANCEL và chỉ chủ đơn hàng
    @PreAuthorize("hasAuthority('ORDER_CANCEL') and (@orderRepository.findById(#orderId).get().user.username == authentication.name)")
    public OrderResponse cancelOrder(String orderId) {
        try {
            // Kiểm tra đơn hàng tồn tại
            var order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus() == OrderStatus.CANCELLED) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Đơn hàng đã bị hủy trước đó");
            }

            if (order.getStatus() == OrderStatus.DELIVERED) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Không thể hủy đơn hàng đã giao thành công");
            }

            if (order.getStatus() == OrderStatus.SHIPPED) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Không thể hủy đơn hàng đang giao");
            }

            // Cập nhật trạng thái đơn hàng
            order.setStatus(OrderStatus.CANCELLED);
            var savedOrder = orderRepository.save(order);

            if (savedOrder == null) {
                throw new AppException(ErrorCode.UNCATCH_ERROR, "Lỗi khi lưu đơn hàng");
            }

            return orderMapper.toOrderResponse(savedOrder);
        } catch (AppException e) {
            log.error("Error in cancelOrder: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in cancelOrder: ", e);
            throw new AppException(ErrorCode.UNCATCH_ERROR, "Lỗi không xác định khi hủy đơn hàng");
        }
    }

    // Duyệt đơn hàng - quyền ORDER_APPROVE (ADMIN và SHOP)
    @PreAuthorize("hasAuthority('ORDER_APPROVE')")
    public OrderResponse approveOrder(String orderId) {
        try {
            // Kiểm tra đơn hàng tồn tại
            var order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            // Kiểm tra trạng thái đơn hàng
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Chỉ có thể duyệt đơn hàng đang chờ xử lý");
            }

            // Cập nhật trạng thái đơn hàng
            order.setStatus(OrderStatus.PROCESSING);
            var savedOrder = orderRepository.save(order);

            if (savedOrder == null) {
                throw new AppException(ErrorCode.UNCATCH_ERROR, "Lỗi khi lưu đơn hàng");
            }

            return orderMapper.toOrderResponse(savedOrder);
        } catch (AppException e) {
            log.error("Error in approveOrder: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in approveOrder: ", e);
            throw new AppException(ErrorCode.UNCATCH_ERROR, "Lỗi không xác định khi duyệt đơn hàng");
        }
    }
}
