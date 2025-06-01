package com.thoai.ecommerce_service.mapper;

import com.thoai.ecommerce_service.dto.request.AddressCreateRequest;
import com.thoai.ecommerce_service.dto.response.AddressReponse;
import com.thoai.ecommerce_service.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "user", ignore = true)
    Address toAddress(AddressCreateRequest request);

    @Mapping(source = "user.userId", target = "userId")
    AddressReponse toAddressResponse(Address address);

}
