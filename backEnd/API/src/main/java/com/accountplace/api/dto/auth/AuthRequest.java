package com.accountplace.api.dto.auth;


import lombok.*;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String identifier;
    private String password;
}
