package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
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

    // Tạo permission - chỉ admin có quyền PERMISSION_CREATE
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public PermissionResponse createPermission(PermissionRequest request) {
        log.info("Creating permission: {}", request.getName());
        var permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    // Lấy danh sách permissions - quyền PERMISSION_READ
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public List<PermissionResponse> getPermissions() {
        log.info("Getting all permissions");
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    // Xóa permission - chỉ admin có quyền PERMISSION_DELETE
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public void deletePermission(String permissionName) {
        log.info("Deleting permission: {}", permissionName);
        permissionRepository.deleteById(permissionName);
    }
}
