package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thoai.ecommerce_service.dto.request.PermissionRequest;
import com.thoai.ecommerce_service.dto.response.PermissionResponse;
import com.thoai.ecommerce_service.mapper.PermissionMapper;
import com.thoai.ecommerce_service.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    // Tạo quyền mới
    public PermissionResponse createPermission(PermissionRequest request) {
        var permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    // Lấy danh sách quyền
    public List<PermissionResponse> getPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    // Xóa quyền
    public void deletePermission(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
