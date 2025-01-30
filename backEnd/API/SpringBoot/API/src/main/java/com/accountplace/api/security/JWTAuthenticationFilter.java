package com.accountplace.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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
 * This filter checks if the request contains a valid JWT, extracts the username from it,
 * loads the corresponding user details, and sets the authentication context for the request.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider tokenGenerator;

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

        // Extract JWT from the request
        String token = getJWTFromRequest(request);
        System.out.println("gotten from request: " + token);

        // Validate the token and extract user information
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            System.out.println("validated token: " + token);
            String username = tokenGenerator.getUsernameFromJWT(token);
            System.out.println("username: " + username);

            // Load user details using the username from the JWT
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            System.out.println("userDetails: " + userDetails);

            // Create an authentication token with user details and authorities
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            System.out.println("authenticationToken: " + authenticationToken);

            // Set additional details for authentication
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println("authenticated token 2: " + authenticationToken);

            // Set the authentication token in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("Authorities set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            System.out.println("Authorities from UserDetails: " + userDetails.getAuthorities());
            System.out.println("Current SecurityContext: " + SecurityContextHolder.getContext());
        }

        // Log after setting context
        System.out.println("After setting authentication: " + SecurityContextHolder.getContext());

        // Proceed with the filter chain
        filterChain.doFilter(request, response);

        // Log after filter chain
        System.out.println("After filter chain: " + SecurityContextHolder.getContext());
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request The incoming HTTP request.
     * @return The JWT token, or null if no token is found.
     */
    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
