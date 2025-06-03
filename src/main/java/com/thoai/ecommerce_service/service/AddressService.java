package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.AddressMapper;
import com.thoai.ecommerce_service.repository.AddressRepository;
import com.thoai.ecommerce_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    UserRepository userRepository;

    // Tạo mới địa chỉ
    // Chỉ cho phép người dùng tao thông tin của chính mình hoặc người dùng có
    // quyền ADMIN
    public AddressReponse createAddress(AddressCreateRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var address = addressMapper.toAddress(request);
        address.setUser(user); // // Gán đối tượng User vào Entity Address
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    // Lấy danh sách địa chỉ
    @PreAuthorize("hasRole('ADMIN')")
    public List<AddressReponse> getAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    // Lấy danh sách địa chỉ theo userId
    @PreAuthorize("hasRole('ADMIN')")
    public List<AddressReponse> getAddressesByUserId(String userId) {
        var addresses = addressRepository.findByUser_UserId(userId);
        return addresses.stream().map(addressMapper::toAddressResponse).toList();
    }

    // User tự lấy danh sách địa chỉ của mình
    public List<AddressReponse> getMyAddresses() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        var addresses = addressRepository.findByUser_UserId(user.getUserId());
        return addresses.stream().map(addressMapper::toAddressResponse).toList();
    }

    // Xóa địa chỉ
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAddress(String addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        addressRepository.deleteById(addressId);
    }
}
