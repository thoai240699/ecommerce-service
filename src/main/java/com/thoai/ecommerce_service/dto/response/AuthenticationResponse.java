package com.thoai.ecommerce_service.dto.response;

public class AuthenticationResponse {
    private  String token;
    boolean authenticated;

    public boolean isAuthenticated() {
        return authenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
