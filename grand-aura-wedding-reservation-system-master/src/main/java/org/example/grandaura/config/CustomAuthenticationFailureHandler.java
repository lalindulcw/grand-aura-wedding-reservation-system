package org.example.grandaura.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom authentication failure handler
 * Redirects users back to the appropriate login page on authentication failure
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     AuthenticationException exception) throws IOException, ServletException {
        
        // Get the referer URL to determine which login page was used
        String referer = request.getHeader("Referer");
        
        if (referer != null && referer.contains("/hotel-owner/login")) {
            // If the request came from hotel owner login page, redirect back there
            response.sendRedirect("/hotel-owner/login?error=true");
        } else if (referer != null && referer.contains("/event-coordinator/login")) {
            // If the request came from event coordinator login page, redirect back there
            response.sendRedirect("/event-coordinator/login?error=true");
        } else if (referer != null && referer.contains("/system-admin/login")) {
            // If the request came from system administrator login page, redirect back there
            response.sendRedirect("/system-admin/login?error=true");
        } else if (referer != null && referer.contains("/catering-manager/login")) {
            // If the request came from catering manager login page, redirect back there
            response.sendRedirect("/catering-manager/login?error=true");
        } else if (referer != null && referer.contains("/front-desk/login")) {
            // If the request came from front desk login page, redirect back there
            response.sendRedirect("/front-desk/login?error=true");
        } else {
            // Otherwise, redirect to regular login page
            response.sendRedirect("/login?error=true");
        }
    }
}
