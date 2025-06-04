package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.RoleRequest;
import com.thoai.ecommerce_service.dto.response.RoleResponse;
import com.thoai.ecommerce_service.mapper.RoleMapper;
import com.thoai.ecommerce_service.repository.PermissionRepository;
import com.thoai.ecommerce_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    // Tạo role - chỉ admin có quyền ROLE_CREATE
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public RoleResponse createRole(RoleRequest request) {
        log.info("Creating role: {}", request.getName());
        var role = roleMapper.toRole(request);

        // Gán permissions cho role
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    // Lấy danh sách roles - quyền ROLE_READ
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public List<RoleResponse> getRoles() {
        log.info("Getting all roles");
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    // Xóa role - chỉ admin có quyền ROLE_DELETE
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public void deleteRole(String role) {
        log.info("Deleting role: {}", role);
        roleRepository.deleteById(role);
    }

    // Cập nhật role - quyền ROLE_UPDATE
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public RoleResponse updateRole(String roleName, RoleRequest request) {
        log.info("Updating role: {}", roleName);
        var role = roleRepository.findById(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setDescription(request.getDescription());

        // Cập nhật permissions
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
}
