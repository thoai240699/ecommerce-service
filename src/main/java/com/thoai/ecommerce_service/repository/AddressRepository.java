package com.thoai.ecommerce_service.repository;

import com.thoai.ecommerce_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
