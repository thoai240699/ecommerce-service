package com.thoai.ecommerce_service.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "vai_tro")
public class Role {
    @Column(name = "ten_vai_tro")
    @Id
    String name;

    @Column(name = "mo_ta")
    String description;

    @ManyToMany
    @JoinTable(
            name = "vai_tro_quyen_han",
            joinColumns = @JoinColumn(name = "ten_vai_tro"),
            inverseJoinColumns = @JoinColumn(name = "ten_quyen_han"))
    Set<Permission> permissions;
}
