package com.thoai.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thoai.ecommerce_service.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    // Custom query methods can be defined here if needed
    // For example, to find a permission by its name:
    // Optional<Permission> findByName(String name);
}
