package com.accountplace.api.tools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

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
}
