package com.thoai.ecommerce_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

// Để định nghĩa các mã lỗi
@Getter
public enum ErrorCode {
    UNCATCH_ERROR(2499, "Lỗi chưa xác định.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTS(2401, "Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2402, "Không tìm thấy người dùng với mã người dùng này", HttpStatus.NOT_FOUND),
    USERNAME_NOT_FOUND(2403, "Không tìm thấy người dùng với tên đăng nhập này", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH(2404, "Mật khẩu không đúng, hãy nhập lại mật khẩu", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTS(2405, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(2406, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(2407, "Bạn chưa đăng nhập", HttpStatus.UNAUTHORIZED),
    ADDRESS_NOT_FOUND(2408, "Không tìm thấy địa chỉ", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(2409, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    ADMIN_NOT_FOUND(2410, "Không tìm thấy vai trò ADMIN trong cơ sở dữ liệu", HttpStatus.NOT_FOUND),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
