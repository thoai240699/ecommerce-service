package com.thoai.ecommerce_service.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.request.AddressUpdateRequest;
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

    // Tạo mới địa chỉ - quyền ADDRESS_CREATE và chỉ cho phép tạo địa chỉ cho chính
    // mình
    @PreAuthorize("hasAuthority('ADDRESS_CREATE') and (@userRepository.findById(#request.userId).get().username == authentication.name)")
    public AddressReponse createAddress(AddressCreateRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var address = addressMapper.toAddress(request);
        address.setUser(user);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    // Lấy danh sách tất cả địa chỉ - chỉ ADMIN có quyền ADDRESS_READ_ALL
    @PreAuthorize("hasAuthority('ADDRESS_READ_ALL')")
    public List<AddressReponse> getAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    // Lấy danh sách địa chỉ theo userId - chỉ ADMIN có quyền ADDRESS_READ_ALL
    @PreAuthorize("hasAuthority('ADDRESS_READ_ALL')")
    public List<AddressReponse> getAddressesByUserId(String userId) {
        var addresses = addressRepository.findByUser_UserId(userId);
        return addresses.stream().map(addressMapper::toAddressResponse).toList();
    }

    // User tự lấy danh sách địa chỉ của mình - có quyền ADDRESS_READ
    @PreAuthorize("hasAuthority('ADDRESS_READ')")
    public List<AddressReponse> getMyAddresses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));
        var addresses = addressRepository.findByUser_UserId(user.getUserId());
        return addresses.stream().map(addressMapper::toAddressResponse).toList();
    }

    // Xóa địa chỉ - quyền ADDRESS_DELETE và chỉ cho phép xóa địa chỉ của chính mình
    @PreAuthorize("hasAuthority('ADDRESS_DELETE') and (@addressRepository.findById(#addressId).get().user.username == authentication.name)")
    public void deleteAddress(String addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        addressRepository.deleteById(addressId);
    }

    // Cập nhật địa chỉ - quyền ADDRESS_UPDATE và chỉ cho phép cập nhật địa chỉ của
    // chính mình
    @PreAuthorize("hasAuthority('ADDRESS_UPDATE') and (@addressRepository.findById(#addressId).get().user.username == authentication.name)")
    public AddressReponse updateAddress(String addressId, AddressUpdateRequest request) {
        var address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressMapper.updateAddress(address, request);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
}
