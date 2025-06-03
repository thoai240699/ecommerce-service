package com.thoai.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.thoai.ecommerce_service.dto.request.OrderCreationRequest;
import com.thoai.ecommerce_service.dto.request.OrderUpdateRequest;
import com.thoai.ecommerce_service.dto.response.OrderResponse;
import com.thoai.ecommerce_service.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order toOrder(OrderCreationRequest request);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderUpdateRequest request);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "address.addressId", target = "addressId")
    OrderResponse toOrderResponse(Order order);
}
