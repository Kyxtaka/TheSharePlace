package com.accountplace.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JWTProvider {

    // Token Generator
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(SecurityConstants.JWT_SECRET) // Use the Key object
                .compact();
    }

    //Username extractor
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SecurityConstants.JWT_SECRET) // Use the Key object for signature verification
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    //token validator
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SecurityConstants.JWT_SECRET) // Use the Key object for signature verification
                    .build()
                    .parseSignedClaims(token); // Parse and validate the JWT
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", e);
        }
    }
}
