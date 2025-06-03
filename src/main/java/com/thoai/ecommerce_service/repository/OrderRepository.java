package com.thoai.ecommerce_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thoai.ecommerce_service.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_UserId(String userId);
}
