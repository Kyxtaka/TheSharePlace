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


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider tokenGenerator;

    @Autowired
    private  CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = getJWTFromRequest(request);
        System.out.println("gotten from request : "+token);
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            System.out.println("validated token : "+token);
            String username = tokenGenerator.getUsernameFromJWT(token);
            System.out.println("username : "+username);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            System.out.println("userDetails : "+userDetails);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            System.out.println("authenticationToken : "+authenticationToken);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println("authenticated token 2 : "+authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("Authorities set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            System.out.println("Authorities from UserDetails: " + userDetails.getAuthorities());
            System.out.println("Current SecurityContext: " + SecurityContextHolder.getContext());
        }
        // Log after setting context
        System.out.println("After setting authentication: " + SecurityContextHolder.getContext());
        filterChain.doFilter(request, response);
        // Log after filter chain
        System.out.println("After filter chain: " + SecurityContextHolder.getContext());
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
