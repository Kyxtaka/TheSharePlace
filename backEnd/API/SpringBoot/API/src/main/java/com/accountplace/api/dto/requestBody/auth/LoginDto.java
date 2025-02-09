package com.accountplace.api.dto.requestBody.auth;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for user login requests.
 * This class is used to encapsulate user credentials during authentication.
 */
@Data
public class LoginDto {

    /**
     * The user identifier, which can be either an email or a username.
     */
    private String identifier;

    /**
     * The user's password.
     */
    private String password;
}
