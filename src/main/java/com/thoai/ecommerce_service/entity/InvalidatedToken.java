package com.thoai.ecommerce_service.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "token_da_huy")
public class InvalidatedToken {
    @Id
    @Column(name = "ma_token")
    String id;

    @Column(name = "thoi_gian_het_han")
    Date expiration;
}
