package com.accountplace.api.dto.crud.update;

import com.accountplace.api.entity.Role;
import com.accountplace.api.tools.Email;

import java.io.Serializable;
import java.util.List;

public class UserUpdateDTO implements Serializable {
    /**
     * The unique identifier for the user.
     */
    private Integer id;

    /**
     * The username of the user.
     */
    private String username;



    /**
     * The email address of the user.
     */
    private Email email;

    /**
     * The first name of the user.
     */
    private String firstname;

    /**
     * The last name of the user.
     */
    private String lastname;

    /**
     * The list of roles assigned to the user.
     * Each role defines the user's permissions and access levels.
     */
    private List<Role> roles;
}
