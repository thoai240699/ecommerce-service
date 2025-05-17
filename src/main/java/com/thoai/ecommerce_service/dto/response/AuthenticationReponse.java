package com.thoai.ecommerce_service.dto.response;

public class AuthenticationReponse {
    boolean authenticated;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
