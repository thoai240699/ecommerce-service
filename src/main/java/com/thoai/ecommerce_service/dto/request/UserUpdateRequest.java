package com.thoai.ecommerce_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 8, message = "Mật khẩu phải chứa tối thiểu  8 ký tự.")
    String password;
    String name;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không hợp lệ.")
    String email;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^(0|\\+?84)(3|5|7|8|9)[0-9]{8}$", message = "Số điện thoại phải là số điện thoại viêt nam.")
    String phone;

}
