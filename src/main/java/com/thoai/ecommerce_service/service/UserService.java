package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Tao mới user
    public User createUser(UserCreationRequest request) {
        User user = new User();

        // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Mã hóa mật khẩu theo thuật toán hash BCrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    // Cập nhật thông tin user
    public User updateUser(String userId, UserUpdateRequest request){
        User user = getUser(userId);

        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return userRepository.save(user);
    }

    // Xóa user
    public void userDelete(String userId){
        userRepository.deleteById(userId);
    }

    // Lấy danh sách user
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // Lấy thông tin user theo id
    public User getUser(String id){
        //Trả về một biến. Nếu không tìm thấy báo lỗi/ dùng lambda function
        //Lambda function trong Java (giới thiệu từ Java 8) là cách viết ngắn gọn của một biểu thức hàm (functional interface implementatio
        //(parameters) -> { body }
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
