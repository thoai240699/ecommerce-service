package com.thoai.ecommerce_service.mapper;

import com.thoai.ecommerce_service.dto.request.PermissionRequest;
import com.thoai.ecommerce_service.dto.response.PermissionResponse;
import com.thoai.ecommerce_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
