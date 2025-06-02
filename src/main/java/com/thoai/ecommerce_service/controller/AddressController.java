package com.thoai.ecommerce_service.controller;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/addresses")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    // Tạo địa chỉ
    @PostMapping
    ApiResponse<AddressReponse> createAddress(@RequestBody @Valid AddressCreateRequest request) {
        return ApiResponse.<AddressReponse>builder()
                .result(addressService.createAddress(request))
                .build();
    }

    // Lấy danh sách địa chỉ
    @GetMapping
    ApiResponse<List<AddressReponse>> getAddresses() {
        return ApiResponse.<List<AddressReponse>>builder()
                .result(addressService.getAddresses())
                .build();
    }

    // Lấy địa chỉ theo userId
    @GetMapping("/{userId}")
    ApiResponse<List<AddressReponse>> getAddressesByUserId(@PathVariable("userId") String userId) {
        return ApiResponse.<List<AddressReponse>>builder()
                .result(addressService.getAddressesByUserId(userId))
                .build();
    }
}
