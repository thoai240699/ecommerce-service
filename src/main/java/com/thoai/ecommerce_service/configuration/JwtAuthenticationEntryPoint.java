package com.thoai.ecommerce_service.configuration;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Lớp này dùng để custom phản hồi khi người dùng chưa xác thực 401 Unauthorized
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    // Phương thức này sẽ được gọi khi có lỗi xác thực (401)
    public void commence(
            HttpServletRequest request, // Đối tượng đại diện cho request từ client
            HttpServletResponse response, // Đối tượng đại diện cho response trả về client
            AuthenticationException authException) // Ngoại lệ xác thực
            throws IOException, ServletException {
        // Lấy mã lỗi, thông báo và status code cho lỗi chưa xác thực từ enum ErrorCode
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        // Đặt mã trạng thái HTTP cho response
        response.setStatus(errorCode.getStatusCode().value());
        // Đặt kiểu dữ liệu trả về là JSON
                response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

        // Tạo đối tượng ApiResponse với mã lỗi và thông báo lỗi
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        // Tạo ObjectMapper để chuyển đổi object Java sang JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Ghi chuỗi JSON vào response trả về client
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        // Đảm bảo dữ liệu đã được ghi hết ra response
        response.flushBuffer();
    }
}
