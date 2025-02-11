package com.accountplace.api.dto.response.auth;

import lombok.Getter;

/**
 * Data Transfer Object (DTO) for the authentication response.
 * This class is used to return the authentication token to the client.
 */
@Getter
public class AuthResponseDto {

    /**
     * The access token issued upon successful authentication.
     */
    private String accessToken;

    /**
     * The type of token, which is always "Bearer" for JWT tokens.
     * This field is constant.
     */
    private final String tokenType = "Bearer";

    /**
     * Constructor to create an instance of AuthResponseDto with the provided token.
     *
     * @param accessToken The authentication token to be returned in the response.
     */
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets the authentication token.
     *
     * @return The access token.
     */
    public String getToken() {
        return accessToken;
    }

    /**
     * Sets a new authentication token.
     *
     * @param token The new token to be set.
     */
    public void setToken(String token) {
        this.accessToken = token;
    }
}
