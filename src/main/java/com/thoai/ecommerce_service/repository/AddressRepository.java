package com.thoai.ecommerce_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thoai.ecommerce_service.entity.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser_UserId(String userId);
}
