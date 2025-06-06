package com.thoai.ecommerce_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.request.AddressUpdateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.dto.response.ApiResponse;
import com.thoai.ecommerce_service.service.AddressService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    // User tự lấy danh sách địa chỉ của mình - quyền ADDRESS_READ
    @GetMapping("/myAddresses")
    ApiResponse<List<AddressReponse>> getMyAddresses() {
        return ApiResponse.<List<AddressReponse>>builder()
                .result(addressService.getMyAddresses())
                .build();
    }

    // Xóa địa chỉ - quyền ADDRESS_DELETE
    @DeleteMapping("/{addressId}")
    ApiResponse<String> deleteAddress(@PathVariable("addressId") String addressId) {
        addressService.deleteAddress(addressId);
        return ApiResponse.<String>builder()
                .result("Địa chỉ đã được xóa thành công")
                .build();
    }

    // Cập nhật địa chỉ - quyền ADDRESS_UPDATE
    @PutMapping("/{addressId}")
    ApiResponse<AddressReponse> updateAddress(
            @PathVariable("addressId") String addressId, @RequestBody @Valid AddressUpdateRequest request) {
        return ApiResponse.<AddressReponse>builder()
                .result(addressService.updateAddress(addressId, request))
                .build();
    }
}
