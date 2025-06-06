package com.thoai.ecommerce_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thoai.ecommerce_service.dto.response.ApiResponse;

// @ControllerAdvice: Để xử lý các exception toàn cục trong ứng dụng
@ControllerAdvice
public class GlobalExceptionHandler {

    // Lỗi không xác định
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATCH_ERROR.getCode());
        apiResponse.setMessage(ErrorCode.UNCATCH_ERROR.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // Lỗi truy cập bị từ chối
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // ValidationException: Để xử lý các lỗi xác thực dữ liệu đầu vào
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // Lỗi thông tin không hợp lệ
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage(e.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
