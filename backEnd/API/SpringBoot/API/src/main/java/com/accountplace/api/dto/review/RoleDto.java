package com.accountplace.api.dto.review;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a role.
 * This class encapsulates the details of a role, including its ID and name.
 */
@Data
public class RoleDto {

    /**
     * The unique identifier for the role.
     */
    private int id;

    /**
     * The name of the role.
     */
    private String name;

    // /**
    //  * A brief description of the role.
    //  * Uncomment if role descriptions are needed in the future.
    //  */
    // private String description;
}
