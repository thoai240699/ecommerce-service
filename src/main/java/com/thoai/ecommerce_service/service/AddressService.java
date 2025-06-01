package com.thoai.ecommerce_service.service;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.mapper.AddressMapper;
import com.thoai.ecommerce_service.repository.AddressRepository;
import com.thoai.ecommerce_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
    // @PostAuthorize("returnObject.username == authentication.name or
    // hasRole('ADMIN')")
    public AddressReponse createAddress(AddressCreateRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var address = addressMapper.toAddress(request);
        address.setUser(user); //// Gán đối tượng User vào Entity Address
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

}
