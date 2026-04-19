package com.accountplace.api.dto.crud.update;

import com.accountplace.api.tools.Email;
import lombok.*;
import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.dto.crud.update.PublicPlatformDTO;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing an account.
 * This class encapsulates the details of a shared accounts, including personal information and associated group/platform.
 */
@Data
@AllArgsConstructor
@ToString
public class AccountUpdateDTO implements Serializable {

    /**
     * The unique identifier of the account.
     */
    private Integer id;

    /**
     * The username associated with the account.
     */
    private String username;

    /**
     * The password associated with the account.
     * Can only be viewed for by logged user
     */
    private String password;

    /**
     * The email address associated with the account.
     */
    private Email email;

    /**
     * A flag indicating whether two-factor authentication (2FA) is enabled for the account.
     */
    private boolean a2f;

    /**
     * The group associated with the account.
     */
    private PublicGroupDTO group;

    /**
     * The platform associated with the account.
     */
    private PublicPlatformDTO platform;
}
