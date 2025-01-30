package com.accountplace.api.dto.auth;

import lombok.*;

/**
 * Data Transfer Object (DTO) for authentication requests.
 * This class is used to encapsulate user login credentials.
 */
@Data
@AllArgsConstructor
public class AuthRequest {

    /**
     * The user identifier, which can be either an email or a username.
     */
    private String identifier;

    /**
     * The user's password.
     */
    private String password;
}
