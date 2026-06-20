package com.accountplace.api.security.jwt;

import com.accountplace.api.tools.NetworkToolsLib;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;



@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final JwtTokenProperties jwtTokenProperties;

//    public JWTProvider(JwtTokenProperties jwtTokenProperties) {
//        this.jwtTokenProperties = jwtTokenProperties;
//    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtTokenProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Calculate of the token validity time
    private long getExpirationMillis() {
        return switch (jwtTokenProperties.unit().toLowerCase()) {
            case "sec"  -> Duration.ofSeconds(jwtTokenProperties.expiration()).toMillis();
            case "min"  -> Duration.ofMinutes(jwtTokenProperties.expiration()).toMillis();
            case "hour" -> Duration.ofHours(jwtTokenProperties.expiration()).toMillis();
            case "day"  -> Duration.ofDays(jwtTokenProperties.expiration()).toMillis();
            default -> throw new IllegalArgumentException("Unknown unit: " + jwtTokenProperties.unit());
        };
    }

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
        Date expirationDate = new Date(currentDate.getTime() + getExpirationMillis());;
        HashMap<String, Object> claims = new HashMap<>();
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
                .signWith(this.getSigningKey())
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
                .verifyWith(this.getSigningKey()) // Use the Key object for signature verification
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public HashMap<String, Object> getDataJWT(String token) {
        Jws<Claims> parser = Jwts.parser()
                .verifyWith(this.getSigningKey())
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
                    .verifyWith(this.getSigningKey()) // Use the Key object for signature verification
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
