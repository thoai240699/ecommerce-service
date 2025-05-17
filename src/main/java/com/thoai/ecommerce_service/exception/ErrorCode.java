package com.thoai.ecommerce_service.exception;

// Để định nghĩa các mã lỗi
public enum ErrorCode {
    UNCATCH_ERROR(9999, "Lỗi không xác định"),
    USER_EXISTS(1001, "Tên đăng nhập đã tồn tại"),
    USER_NOT_FOUND(1002, "Không tìm thấy người dùng với mã người dùng này"),
    USERNAME_NOT_FOUND(1003, "Không tìm thấy người dùng với tên đăng nhập này"),
    PASSWORD_NOT_MATCH(1004, "Mật khẩu không đúng, hãy nhập lại mật khẩu"),
    ;

    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
