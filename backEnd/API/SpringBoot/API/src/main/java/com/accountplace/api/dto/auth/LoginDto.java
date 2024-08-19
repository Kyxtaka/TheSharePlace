package com.accountplace.api.dto.auth;


import lombok.Data;

@Data
public class LoginDto {
    private String identifier;
    private String password;
}
