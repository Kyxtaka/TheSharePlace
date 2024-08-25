package com.accountplace.api.security.customexception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Custom behavior when access is denied
        // For example, send a custom error message or redirect to an error page
        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"Access Forbidden: " + accessDeniedException.getMessage() + "\"}");
//        response.getWriter().flush();
    }
}