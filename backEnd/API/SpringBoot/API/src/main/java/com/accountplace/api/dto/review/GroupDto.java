package com.accountplace.api.dto.review;

import lombok.*;

/**
 * Data Transfer Object (DTO) representing a group.
 * This class encapsulates the details of a group, including its ID, UID, name, description, and password.
 */
@Data
@AllArgsConstructor
public class GroupDto {

    /**
     * The unique identifier for the group.
     */
    private Integer id;

    /**
     * The unique UID of the group.
     * This can be used to identify the group across different systems or services.
     */
    private Long UID;

    /**
     * The name of the group.
     */
    private String name;

    /**
     * A brief description of the group.
     */
    private String description;

    /**
     * The password associated with the group, if required.
     */
    private String password;
}
