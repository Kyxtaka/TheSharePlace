package com.accountplace.api.dto.crud.update;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoleUpdateDTO implements Serializable {
    /**
     * The unique identifier for the role.
     */
    private int id;

    /**
     * The name of the role.
     */
    private String name;
}
