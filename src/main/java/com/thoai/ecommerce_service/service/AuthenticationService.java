package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    // Phương thức này sẽ nhận vào một đối tượng AuthenticationRequest và trả về true nếu thông tin xác thực hợp lệ, ngược lại trả về false.
    public boolean authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(9);
        // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu người dùng nhập vào hay không
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return true;
        } else {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
