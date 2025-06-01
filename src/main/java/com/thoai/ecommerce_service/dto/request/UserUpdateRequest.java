package com.thoai.ecommerce_service.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 8, message = "Mật khẩu phải chứa tối thiểu  8 ký tự.")
    String password;

    String name;

    @Email(message = "Email không hợp lệ.")
    String email;

    @Pattern(regexp = "^(0|\\+?84)(3|5|7|8|9)[0-9]{8}$", message = "Số điện thoại phải là số điện thoại viêt nam.")
    String phone;

    List<String> roles;
}
