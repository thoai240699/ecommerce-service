package com.thoai.ecommerce_service.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponse {
    private  String token;
    boolean authenticated;

}
