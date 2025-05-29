package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.RoleRequest;
import com.thoai.ecommerce_service.dto.response.RoleResponse;
import com.thoai.ecommerce_service.mapper.RoleMapper;
import com.thoai.ecommerce_service.repository.PermissionRepository;
import com.thoai.ecommerce_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    // Tạo role mới
    public RoleResponse createRole(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    // Lấy danh sách role
    public List<RoleResponse> getRoles(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    // Xóa role
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
