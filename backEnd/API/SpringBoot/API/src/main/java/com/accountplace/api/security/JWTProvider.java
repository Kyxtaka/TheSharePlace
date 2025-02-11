package com.accountplace.api.security;

import com.accountplace.api.tools.NetworkToolsLib;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class JWTProvider {

    /**
     * Generates a JWT token for the given authentication object.
     * The token contains the identifier, issued at date, and expiration date.
     *
     * @param authentication The authentication object containing the user's details.
     * @return A JWT token as a String.
     */
    public String generateToken(Authentication authentication, String rawIP, String userAgent) {
        String identifier = authentication.getName();
        String hashIP = NetworkToolsLib.hashSHA256(rawIP);
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        HashMap<String, Object> claims = new HashMap<>();
        //claims.put("identifier", identifier);
        claims.put("rawIP", rawIP);
        claims.put("hashIP", hashIP);
        claims.put("userAgent", userAgent);
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("use", "BEARER");

        //builder.header().add(headers);

        return Jwts.builder()
                .header()
                    .add(headers)
                    .and()
                .claims(claims)
                .subject(identifier)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(SecurityConstants.JWT_SECRET)
                .compact();
    }

    /**
     * Extracts the identifier from the given JWT token.
     *
     * @param token The JWT token from which the identifier will be extracted.
     * @return The identifier as a String.
     */
    public String getIdentifierFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SecurityConstants.JWT_SECRET) // Use the Key object for signature verification
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public HashMap<String, Object> getDataJWT(String token) {
        Jws<Claims> parser = Jwts.parser()
                .verifyWith(SecurityConstants.JWT_SECRET)
                .build()
                .parseSignedClaims(token);
        HashMap<String, Object> data = new HashMap<>();
        data.put("header",parser.getHeader());
        data.put("claims",parser.getPayload());
        return data;
    }

    /**
     * Validates the given JWT token by checking its signature and expiration.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, otherwise false.
     * @throws AuthenticationCredentialsNotFoundException If the token is invalid or expired.
     */
    public boolean validateTokenTimeValidity(String token) {
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

    public boolean validateDeviceTokenMatches(String token, HttpServletRequest request) {
        HashMap<String, Object> data = this.getDataJWT(token);
        String requestDeviceHashIP = NetworkToolsLib.hashSHA256(NetworkToolsLib.getClientIpAddress(request));
        String requestDeviceUserAgent = NetworkToolsLib.getUserAgent(request);
        Claims tokenClaims = (Claims) data.get("claims");
        String hashTokenIP = tokenClaims.get("hashIP").toString();
        String tokenUserAgent = tokenClaims.get("userAgent").toString();
        return requestDeviceHashIP.equals(hashTokenIP) && requestDeviceUserAgent.equals(tokenUserAgent);
    }
}
