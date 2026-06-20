package com.accountplace.api.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.refresh")
public record JwtRefreshProperties(
        String secret,
        long expiration,
        String unit
) {}