package com.accountplace.api.dto.review;

import com.accountplace.api.tools.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object (DTO) representing an account.
 * This class encapsulates the details of a shared accounts, including personal information and associated group/platform.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AccountDTO {

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
     * In production, ensure that this field is securely handled and stored.
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
    private GroupDto group;

    /**
     * The platform associated with the account.
     */
    private PlatformDto platform;
}
