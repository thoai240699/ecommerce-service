package com.thoai.ecommerce_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thoai.ecommerce_service.dto.request.RoleRequest;
import com.thoai.ecommerce_service.dto.response.RoleResponse;
import com.thoai.ecommerce_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true) // Tu map do permission la set string
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
