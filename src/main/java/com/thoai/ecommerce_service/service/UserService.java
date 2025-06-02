package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.constant.PredefinedRole;
import com.thoai.ecommerce_service.dto.request.UserCreationRequest;
import com.thoai.ecommerce_service.dto.request.UserUpdateRequest;
import com.thoai.ecommerce_service.dto.response.UserResponse;
import com.thoai.ecommerce_service.entity.Role;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.UserMapper;
import com.thoai.ecommerce_service.repository.RoleRepository;
import com.thoai.ecommerce_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    // Tao mới user
    public UserResponse createUser(UserCreationRequest request) {
        // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }
        // Kiểm tra xem email đã tồn tại hay chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        var user = userMapper.toUser(request);
        // Mã hóa mật khẩu theo thuật toán hash BCrypt
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        // Nếu không có roles nào được cung cấp, gán vai trò mặc định là CUSTOMER
        roleRepository.findById(PredefinedRole.CUSTOMER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // Lưu user vào database, check lỗi trùng lặp tên dang nhap khi concurrent
        // try {
        // user = userRepository.save(user);
        // } catch (DataIntegrityViolationException exception) {
        // throw new AppException(ErrorCode.USER_EXISTED);
        // }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Cập nhật thông tin user
    // Chỉ cho phép người dùng cập nhật thông tin của chính mình hoặc người dùng có
    // quyền ADMIN
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Goi repository để lấy danh sách role từ request
        var roles = roleRepository.findAllById(request.getRoles());
        // Chuyển đổi danh sách role thành HashSet và gán cho user
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Xóa user
    // Chỉ cho phép người dùng có quyền ADMIN xóa người dùng
    @PreAuthorize("hasRole('ADMIN')")
    public void userDelete(String userId) {
        userRepository.deleteById(userId);
    }

    // Lấy danh sách user
    // Chỉ cho phép người dùng có quyền ADMIN xem danh sách người dùng
    @PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    // Lấy thông tin user theo tên đăng nhập
    public UserResponse getMyInformation() {
        // Lấy thông tin người dùng từ SecurityContextHolder
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        // Tìm kiếm người dùng theo tên đăng nhập
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        // Chuyển đổi sang UserResponse và trả về
        return userMapper.toUserResponse(user);
    }

    // Lấy thông tin user theo id
    // Chỉ cho phép người dùng xem thông tin của chính mình hoặc người dùng có quyền
    // ADMIN
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        // Trả về một biến. Nếu không tìm thấy báo lỗi/ dùng lambda function
        // Lambda function trong Java (giới thiệu từ Java 8) là cách viết ngắn gọn của
        // một biểu thức hàm (functional
        // interface implementatio
        // (parameters) -> { body }
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
}
