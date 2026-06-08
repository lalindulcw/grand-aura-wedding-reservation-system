package org.example.grandaura.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom authentication success handler
 * Redirects users based on their role after successful login
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     Authentication authentication) throws IOException, ServletException {
        
        // Check user roles and redirect accordingly
        boolean isHotelOwner = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_HOTEL_OWNER"));
        
        boolean isEventCoordinator = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EVENT_COORDINATOR"));
        
        boolean isSystemAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SYSTEM_ADMIN"));
        
        boolean isCateringManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CATERING_MANAGER"));
        
        boolean isFrontDesk = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_FRONT_DESK"));
        
        if (isHotelOwner) {
            response.sendRedirect("/hotel-owner/dashboard");
        } else if (isEventCoordinator) {
            response.sendRedirect("/event-coordinator/dashboard");
        } else if (isSystemAdmin) {
            response.sendRedirect("/system-admin/dashboard");
        } else if (isCateringManager) {
            response.sendRedirect("/catering-manager/dashboard");
        } else if (isFrontDesk) {
            response.sendRedirect("/front-desk/dashboard");
        } else {
            response.sendRedirect("/bookings/my");
        }
    }
}
