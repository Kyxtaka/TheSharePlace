package com.accountplace.api.dto.response.auth;

import lombok.Getter;

/**
 * Data Transfer Object (DTO) for the authentication response.
 * This class is used to return the authentication token to the client.
 */
@Getter
public class AuthResponseDTO {

    /**
     * The access token issued upon successful authentication.
     */
    private final String accessToken;

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
    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
