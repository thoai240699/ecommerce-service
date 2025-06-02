package com.thoai.ecommerce_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String name;
    String email;
    String phone;

    Set<RoleResponse> roles;
    LocalDateTime createdAt;
}
