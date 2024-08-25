package com.accountplace.api.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterAccountBodyDTO {
    private String username;
    private String email;
    private String password;
    @JsonProperty("A2F")
    private Boolean a2f;
}
