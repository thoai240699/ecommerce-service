package com.thoai.ecommerce_service.controller;

import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Chức năng này sẽ tạo mới một người dùng
    // @PostMapping: Để tạo mới user
    // @RequestBody: Để mapping dữ liệu từ  request body vào UserCreationRequest
    // @Valid: Để kiểm tra dữ liệu đầu vào có hợp lệ hay không
    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    // Chức năng này sẽ lấy danh sách người dùng
    @GetMapping
    List<User> getUsers(){
    return userService.getUsers();
    }

    // @PathVariable: Để mapping dữ liệu từ request url vào biến userId
    @GetMapping("/{userId}")
   User getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    // Chức năng này sẽ cập nhật thông tin người dùng
    // @PutMapping: Để cập nhật thông tin user
    // @RequestBody: Để mapping dữ liệu từ request body vào UserUpdateRequest
    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId , @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    // Chức năng này sẽ xóa người dùng
    // @DeleteMapping: Để xóa user
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.userDelete(userId);
        return "Người dùng đã được xóa thành công.";
    }
}
