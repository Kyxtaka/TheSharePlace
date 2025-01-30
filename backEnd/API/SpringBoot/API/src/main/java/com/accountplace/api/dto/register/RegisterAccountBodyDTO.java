package com.accountplace.api.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * This class encapsulates the necessary information for creating a new user account.
 */
@Data
public class RegisterAccountBodyDTO {

    /**
     * The unique username chosen by the user.
     */
    private String username;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's chosen password.
     */
    private String password;

    /**
     * Indicates whether two-factor authentication (2FA) is enabled.
     * Mapped to JSON property "A2F".
     */
    @JsonProperty("A2F")
    private Boolean a2f;
}
