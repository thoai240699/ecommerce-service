package com.thoai.ecommerce_service.repository;

import com.thoai.ecommerce_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser_UserId(String userId);
}
