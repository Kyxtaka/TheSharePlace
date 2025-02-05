package com.accountplace.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JWTProvider {

    /**
     * Generates a JWT token for the given authentication object.
     * The token contains the username, issued at date, and expiration date.
     *
     * @param authentication The authentication object containing the user's details.
     * @return A JWT token as a String.
     */
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

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token from which the username will be extracted.
     * @return The username as a String.
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SecurityConstants.JWT_SECRET) // Use the Key object for signature verification
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Validates the given JWT token by checking its signature and expiration.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, otherwise false.
     * @throws AuthenticationCredentialsNotFoundException If the token is invalid or expired.
     */
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
