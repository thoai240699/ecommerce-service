package com.thoai.ecommerce_service.entity;

import com.thoai.ecommerce_service.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "don_hang")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_don_hang")
    String orderId;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "ma_dia_chi", nullable = false)
    Address address;

    @Column(name = "tong_tien", nullable = false)
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    OrderStatus status;

    @Column(name = "thoi_gian_tao", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
