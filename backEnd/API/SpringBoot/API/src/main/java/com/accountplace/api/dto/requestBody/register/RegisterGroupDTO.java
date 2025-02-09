package com.accountplace.api.dto.requestBody.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for registering a new group.
 * This class encapsulates the necessary information for creating a group.
 */
@Data
public class RegisterGroupDTO {

    /**
     * The name of the group.
     */
    @JsonProperty("groupName")
    private String groupName;

    /**
     * The password required to join the group.
     */
    @JsonProperty("groupPassword")
    private String groupPassword;

    /**
     * A brief description of the group.
     */
    @JsonProperty("groupDescription")
    private String groupDescription;
}
