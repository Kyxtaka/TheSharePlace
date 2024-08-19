package com.accountplace.api.dto.register;

import lombok.Data;

@Data
public class RegisterUserBodyDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
//    private List<Role> roles;
}
