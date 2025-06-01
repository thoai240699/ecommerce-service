package com.thoai.ecommerce_service.mapper;

import org.mapstruct.Mapper;

import com.thoai.ecommerce_service.dto.request.PermissionRequest;
import com.thoai.ecommerce_service.dto.response.PermissionResponse;
import com.thoai.ecommerce_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
