package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.dto.response.UserResponse;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.UserMapper;
import com.thoai.ecommerce_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // Tao mới user
    public UserResponse createUser(UserCreationRequest request) {
        // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        // Kiểm tra xem email đã tồn tại hay chưa
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

//        user.setUsername(request.getUsername());
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPhone(request.getPhone());
        User user = userMapper.toUser(request);
        // Mã hóa mật khẩu theo thuật toán hash BCrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Cập nhật thông tin user
    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
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
    public UserResponse getUser(String id){
        //Trả về một biến. Nếu không tìm thấy báo lỗi/ dùng lambda function
        //Lambda function trong Java (giới thiệu từ Java 8) là cách viết ngắn gọn của một biểu thức hàm (functional interface implementatio
        //(parameters) -> { body }
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
}
