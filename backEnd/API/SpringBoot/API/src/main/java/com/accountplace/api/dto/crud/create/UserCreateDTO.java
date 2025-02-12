package com.accountplace.api.dto.crud.create;

import com.accountplace.api.dto.crud.pub.PublicRoleDTO;
import com.accountplace.api.entity.RoleEntity;
import com.accountplace.api.tools.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class UserCreateDTO implements Serializable {

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The first name of the user.
     */
    private String firstname;

    /**
     * The last name of the user.
     */
    private String lastname;

    private List<String> roles;
}
