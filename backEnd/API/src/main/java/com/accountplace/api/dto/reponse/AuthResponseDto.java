package com.accountplace.api.dto.reponse;

import lombok.Getter;
@Getter
public class AuthResponseDto {
    private String accessToken;
    private final String tokenType = "Bearer";


    public AuthResponseDto(String token) {
        this.accessToken = token;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String token) {
        this.accessToken = token;
    }
}