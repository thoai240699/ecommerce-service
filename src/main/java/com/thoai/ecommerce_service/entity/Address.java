package com.thoai.ecommerce_service.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "dia_chi")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_dia_chi")
    String addressId;

    // mỗi đối tượng Address được liên kết với một đối tượng User cụ thể
    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    User user;

    @Column(name = "so_nha")
    String streetAddress;

    @Column(name = "phuong_xa")
    String ward;

    @Column(name = "quan_huyen")
    String district;

    @Column(name = "tinh_thanh")
    String city;
}
