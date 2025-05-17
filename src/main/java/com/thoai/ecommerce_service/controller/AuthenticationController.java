package com.thoai.ecommerce_service.controller;

import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.dto.response.AuthenticationReponse;
import com.thoai.ecommerce_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    // @Autowired: Để tự động khởi tạo AuthenticationService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationReponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationReponse> apiResponse = new ApiResponse<>();
        // Kiểm tra thông tin xác thực
        AuthenticationReponse response = new AuthenticationReponse();
        response.setAuthenticated(authenticationService.authenticate(request));
        apiResponse.setResult(response);
        return apiResponse;
    }
}
