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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        // Nếu không có roles nào được cung cấp, gán vai trò mặc định là SHOP_ROLE
        roleRepository.findById(PredefinedRole.SHOP_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // Cách bắt lỗi tốt hơn, khi tạo nhiều user cùng lúc
        // Lưu user vào database, check lỗi trùng lặp tên dang nhap khi concurrent
        // try {
        // user = userRepository.save(user);
        // } catch (DataIntegrityViolationException exception) {
        // throw new AppException(ErrorCode.USER_EXISTED);
        // }

        return userMapper.toUserResponse(userRepository.save(user));
    }



// Cập nhật user - chỉ chính user đó hoặc admin có quyền USER_UPDATE
@PostAuthorize("returnObject.username == authentication.name or hasAuthority('USER_UPDATE')")
public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

    userMapper.updateUser(user, request);
    if (StringUtils.hasText(request.getPassword())) {
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
}

// Xóa user - chỉ admin có quyền USER_DELETE
@PreAuthorize("hasAuthority('USER_DELETE')")
public void userDelete(String userId) {
    if (!userRepository.existsById(userId)) {
        throw new AppException(ErrorCode.USER_NOT_FOUND);
    }
    userRepository.deleteById(userId);
}

// Lấy danh sách tất cả user - chỉ admin có quyền USER_READ_ALL
@PreAuthorize("hasAuthority('USER_READ_ALL')")
public List<UserResponse> getUsers() {
    log.info("In getUsers");
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
}

// Lấy thông tin cá nhân - chỉ cần đăng nhập
@PreAuthorize("hasAuthority('USER_READ')")
public UserResponse getMyInformation() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();

    User user = userRepository.findByUsername(name)
            .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

    return userMapper.toUserResponse(user);
}

// Lấy user theo ID - chỉ chính user đó hoặc admin có quyền USER_READ
@PostAuthorize("returnObject.username == authentication.name or hasAuthority('USER_READ_ALL')")
public UserResponse getUser(String id) {
    log.info("In getUser");
    return userMapper.toUserResponse(
            userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
}
}
