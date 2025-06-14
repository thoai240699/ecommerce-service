package com.thoai.ecommerce_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.thoai.ecommerce_service.dto.request.RoleRequest;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.response.RoleResponse;
import com.thoai.ecommerce_service.service.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    // Tạo role mới - quyền ROLE_CREATE
    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    // Lấy danh sách role - quyền ROLE_READ
    @GetMapping
    ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getRoles())
                .build();
    }

    // Cập nhật role - quyền ROLE_UPDATE
    @PutMapping("/{roleName}")
    ApiResponse<RoleResponse> updateRole(@PathVariable String roleName, @RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(roleName, request))
                .build();
    }

    // Xóa role - quyền ROLE_DELETE
    @DeleteMapping("/{role}")
    ApiResponse<Void> deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);
        return ApiResponse.<Void>builder().build();
    }
}
