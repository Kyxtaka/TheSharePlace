package com.accountplace.api.dto.crud.update;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class GroupUpdateDTO implements Serializable {
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
     * The password of the group.
     */
    private String password;

    /**
     * A brief description of the group.
     */
    private String description;
}
