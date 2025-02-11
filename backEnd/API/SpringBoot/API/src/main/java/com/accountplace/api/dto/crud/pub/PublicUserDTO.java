package com.accountplace.api.dto.crud.pub;

import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.entity.RoleEntity;
import com.accountplace.api.tools.Email;
import lombok.*;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user.
 * This class encapsulates the details of a user, including their ID, username, email, personal information,
 * roles, and associated groups.
 */
@Data
@AllArgsConstructor
public class PublicUserDTO {

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
    private List<PublicRoleDTO> rolesDTO;

    /**
     * The list of groups assigned to the user.
     * Each groups that the user has access
     */
    private List<PublicGroupDTO> groupsDTO;

}
