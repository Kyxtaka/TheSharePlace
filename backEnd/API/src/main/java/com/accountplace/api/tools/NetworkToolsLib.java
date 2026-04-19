package com.accountplace.api.tools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class NetworkToolsLib {

    public static String getClientIpAddress(HttpServletRequest req) {
        // X-Forwarded-For header is used when the application is behind a proxy (e.g., Nginx, AWS ELB)
        String ipAddress = req.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = req.getHeader("Proxy-Client-IP");
            ipAddress = req.getRemoteAddr();
        }

        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }

    public static String getUserAgent(HttpServletRequest req) {
        return req.getHeader("User-Agent");
    }

    public String getLanguage(HttpServletRequest request) {
        return request.getHeader("Accept-Language");
    }

    public static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
            //return new String(hashedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
