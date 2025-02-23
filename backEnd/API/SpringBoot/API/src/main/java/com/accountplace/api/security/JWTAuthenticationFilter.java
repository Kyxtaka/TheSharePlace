package com.accountplace.api.security;

import com.accountplace.api.exceptions.CustomAccessDeniedHandler;
import com.accountplace.api.exceptions.CustomAuthenticationEntryPoint;
import com.accountplace.api.tools.NetworkToolsLib;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for validating and authenticating JWT tokens in incoming HTTP requests.
 * This filter checks if the request contains a valid JWT, extracts the identifier from it,
 * loads the corresponding user details, and sets the authentication context for the request.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * This method processes each incoming request, checking for a valid JWT token in the Authorization header.
     * If a valid token is found, the corresponding user is authenticated and the authentication details
     * are set in the SecurityContext for the current request.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The filter chain to pass the request and response through after processing.
     * @throws ServletException If the request could not be handled.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.getJWTFromRequest(request);// Extract JWT from the request
            if (StringUtils.hasText(token) && jwtProvider.validateTokenTimeValidity(token) && jwtProvider.validateDeviceTokenMatches(token, request)) {
                String identifier = jwtProvider.getIdentifierFromJWT(token);
                this.proceedRequestAuthentication(request, identifier);
            }
        }catch (ExpiredJwtException e){
            System.out.println("JWT Expired");
        }finally {
            filterChain.doFilter(request, response);// Proceed with the filter chain
        }
    }

    protected void proceedRequestAuthentication(HttpServletRequest request, String userIdentifier) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userIdentifier);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // Create an authentication token with user details and authorities
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set additional details for authentication
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Set the authentication token in the SecurityContext
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request The incoming HTTP request.
     * @return The JWT token, or null if no token is found.
     */
    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            Header header = (Header) jwtProvider.getDataJWT(bearerToken).get("header");
            if (header.get("use").toString().equalsIgnoreCase("bearer")) {
                System.out.println(bearerToken);
                return bearerToken;
            }
        }
        return null;
    }

}
