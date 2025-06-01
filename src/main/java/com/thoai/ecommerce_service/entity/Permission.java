package com.thoai.ecommerce_service.entity;

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
@Table(name = "quyen_han")
public class Permission {
    @Id
    @Column(name = "ten_quyen_han")
    String name;

    @Column(name = "mo_ta")
    String description;
}
