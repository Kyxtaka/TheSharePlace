package com.accountplace.api.dto.requestBody.register;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration.
 * This class encapsulates the necessary information for creating a new user account.
 */
@Data
public class RegisterUserBodyDTO {

    /**
     * The first name of the user.
     */
    private String firstname;

    /**
     * The last name of the user.
     */
    private String lastname;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The unique username chosen by the user.
     */
    private String username;

    /**
     * The user's chosen password.
     */
    private String password;

    // /**
    //  * The roles assigned to the user.
    //  * Uncomment if role management is implemented.
    //  */
    // private List<Role> roles;
}
