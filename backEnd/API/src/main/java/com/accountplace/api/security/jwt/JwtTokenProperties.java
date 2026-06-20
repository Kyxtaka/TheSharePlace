package com.accountplace.api.security.jwt;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt.access")
public record JwtTokenProperties(
    String secret,
    long expiration,
    String unit
) {};


