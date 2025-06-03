package com.thoai.ecommerce_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.response.UserResponse;
import com.thoai.ecommerce_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    // Chức năng này sẽ tạo mới một người dùng
    // @PostMapping: Để tạo mới user
    // @RequestBody: Để mapping dữ liệu từ  request body vào UserCreationRequest
    // @Valid: Để kiểm tra dữ liệu đầu vào có hợp lệ hay không
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    // Chức năng này sẽ lấy danh sách người dùng
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // In ra thông tin người dùng đã đăng nhập, khi thuc te se xoa sau
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    // @PathVariable: Để mapping dữ liệu từ request url vào biến userId
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    // Chức năng này sẽ lấy thông tin người dùng hiện tại
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInformation() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInformation())
                .build();
    }

    // Chức năng này sẽ cập nhật thông tin người dùng
    // @PutMapping: Để cập nhật thông tin user
    // @RequestBody: Để mapping dữ liệu từ request body vào UserUpdateRequest
    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    // Chức năng này sẽ xóa người dùng
    // @DeleteMapping: Để xóa user
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.userDelete(userId);
        return ApiResponse.<String>builder()
                .result("Người dùng đã được xóa thành công")
                .build();
    }
}
