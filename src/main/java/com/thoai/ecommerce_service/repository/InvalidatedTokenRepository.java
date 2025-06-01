package com.thoai.ecommerce_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thoai.ecommerce_service.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
