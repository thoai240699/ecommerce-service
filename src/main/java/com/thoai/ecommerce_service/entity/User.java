package com.thoai.ecommerce_service.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
// @Table(name = "nguoi_dung")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // @Column(name = "ma_nguoi_dung")
    String userId;

    // @Column(name = "ten_dang_nhap")
    String username;

    // @Column(name = "mat_khau")
    String password;

    // @Column(name = "ho_ten")
    String name;

    // @Column(name = "email")
    String email;

    //    @Column(name = "so_dien_thoai")
    String phone;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "vai_tro")
    @ManyToMany
    Set<Role> roles;

    // @Column(name = "thoi_gian_tao")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
