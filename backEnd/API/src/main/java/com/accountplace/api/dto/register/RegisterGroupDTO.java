package com.accountplace.api.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterGroupDTO {

    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("groupPassword")
    private String groupPassword;
    @JsonProperty("groupDescription")
    private String groupDescription;

}
