package com.accountplace.api.dto.crud.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing a group.
 * This class encapsulates the details of a group, including its ID, UID, name, description, and password.
 */
@Data
@AllArgsConstructor
@ToString
public class GroupCreateDTO implements Serializable {
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
}
