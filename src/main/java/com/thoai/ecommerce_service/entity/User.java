package com.thoai.ecommerce_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
//    @Column(name = "vai_tro")
//    private VaiTro vaiTro;

    @Column(name = "thoi_gian_tao")
    private LocalDateTime createdAt;

    public User() {
        this.userId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
