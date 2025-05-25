package com.thoai.ecommerce_service.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "nguoi_dung")
public class User {
    @Id
    @Column(name = "ma_nguoi_dung")
    private String userId;

    @Column(name = "ten_dang_nhap")
    private String username;

    @Column(name = "mat_khau")
    private String password;

    @Column(name = "ho_ten")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String phone;

    //    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro")
    private Set<String> roles;

    @Column(name = "thoi_gian_tao")
    private LocalDateTime createdAt;

    public User() {
        this.userId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }


}
