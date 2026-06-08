package com.example.event_planning_dashboard.controller;

import com.example.event_planning_dashboard.Repository.UserRepository;
import com.example.event_planning_dashboard.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Spring Security handles /login GET and POST automatically
    // No explicit mapping needed here

    @GetMapping("/test-users")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testUsers() {
        List<User> users = userRepository.findAll();
        StringBuilder result = new StringBuilder("Users in database:\n");
        for (User user : users) {
            result.append("Username: ").append(user.getUsername())
                  .append(", Role: ").append(user.getRole())
                  .append(", Password Hash: ").append(user.getPassword()).append("\n");
        }
        return result.toString();
    }

    @GetMapping("/test")
    @org.springframework.web.bind.annotation.ResponseBody
    public String test() {
        return "Application is running! Database connection test: " + (userRepository.count() > 0 ? "SUCCESS" : "NO DATA");
    }

    @GetMapping("/cleanup-database")
    @org.springframework.web.bind.annotation.ResponseBody
    public String cleanupDatabase() {
        try {
            // This will be handled by the cleanup script
            return "Database cleanup script created. Please run it manually in H2 console or restart the application to apply changes.";
        } catch (Exception e) {
            return "Error during cleanup: " + e.getMessage();
        }
    }

    @GetMapping("/cleanup")
    public String cleanupPage() {
        return "cleanup";
    }

    @GetMapping("/test-auth")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testAuth() {
        try {
            org.springframework.security.crypto.password.PasswordEncoder encoder = new org.springframework.security.crypto.password.PasswordEncoder() {
                @Override
                public String encode(CharSequence rawPassword) {
                    return rawPassword.toString();
                }
                
                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return rawPassword.toString().equals(encodedPassword);
                }
            };
            
            boolean matches = encoder.matches("password", "password");
            return "Password matching test: " + matches;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/test-login")
    @org.springframework.web.bind.annotation.ResponseBody
    public String testLogin() {
        try {
            // Test user lookup
            List<com.example.event_planning_dashboard.model.User> users = userRepository.findAll();
            StringBuilder result = new StringBuilder("All users:\n");
            for (com.example.event_planning_dashboard.model.User user : users) {
                result.append("Username: ").append(user.getUsername())
                      .append(", Password: ").append(user.getPassword())
                      .append(", Role: ").append(user.getRole()).append("\n");
            }
            
            // Test coordinator user specifically
            List<com.example.event_planning_dashboard.model.User> coordinators = users.stream()
                    .filter(user -> "coordinator".equals(user.getUsername()))
                    .collect(java.util.stream.Collectors.toList());
            
            result.append("\nCoordinator users found: ").append(coordinators.size()).append("\n");
            if (!coordinators.isEmpty()) {
                com.example.event_planning_dashboard.model.User coord = coordinators.get(0);
                result.append("First coordinator - Username: ").append(coord.getUsername())
                      .append(", Password: ").append(coord.getPassword())
                      .append(", Role: ").append(coord.getRole()).append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}

