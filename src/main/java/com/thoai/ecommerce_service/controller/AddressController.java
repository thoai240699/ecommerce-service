package com.thoai.ecommerce_service.controller;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/address")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    // Tạo địa chỉ
    @PostMapping
    ApiResponse<AddressReponse> createAddress(@RequestBody @Valid AddressCreateRequest request)
    {
        return ApiResponse.<AddressReponse>builder()
                .result(addressService.createAddress(request))
                .build();
    }
}
