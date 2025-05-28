package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.PermissionRequest;
import com.thoai.ecommerce_service.dto.response.PermissionResponse;
import com.thoai.ecommerce_service.entity.Permission;
import com.thoai.ecommerce_service.mapper.PermissionMapper;
import com.thoai.ecommerce_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    // Tạo role mới
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    // Lấy danh sách quyền
    public List<PermissionResponse> getRoles(){
        return roleRepository.findAll().stream()
                .map(roleMapper::toroleResponse)
                .toList();
    }

    // Xóa quyền
    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
