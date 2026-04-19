package com.accountplace.api.dto.crud.create;

import lombok.AllArgsConstructor;import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoleCreateDTO implements Serializable {
    /**
     * The name of the role.
     */
    private String name;
}
