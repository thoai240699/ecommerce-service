package com.thoai.ecommerce_service.controller;

import com.nimbusds.jose.JOSEException;
import com.thoai.ecommerce_service.dto.request.IntrospectRequest;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.dto.request.AuthenticationRequest;
import com.thoai.ecommerce_service.dto.response.AuthenticationResponse;
import com.thoai.ecommerce_service.dto.response.IntrospectResponse;
import com.thoai.ecommerce_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    // @Autowired: Để tự động khởi tạo AuthenticationService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
        // Kiểm tra thông tin xác thực
        AuthenticationResponse response;
        response = authenticationService.authenticate(request);
        apiResponse.setResult(response);
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse<IntrospectResponse> apiResponse = new ApiResponse<>();
        IntrospectResponse response;
        response = authenticationService.introspect(request);
        apiResponse.setResult(response);
        return apiResponse;
    }
}
