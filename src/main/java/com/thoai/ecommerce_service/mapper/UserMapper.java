package com.thoai.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.dto.response.UserResponse;
import com.thoai.ecommerce_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true) // Password phải mã hóa trước khi lưu
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true) // Roles sẽ được xử lý riêng do là danh sách chuỗi ma user thi la set
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);
}
