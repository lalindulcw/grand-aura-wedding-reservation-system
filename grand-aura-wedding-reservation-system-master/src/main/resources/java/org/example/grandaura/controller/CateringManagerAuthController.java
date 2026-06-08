package org.example.grandaura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Catering Manager authentication
 * Handles login/logout functionality
 */
@Controller
@RequestMapping("/catering-manager")
public class CateringManagerAuthController {

    /**
     * Catering Manager Login Page
     */
    @GetMapping("/login")
    public String login() {
        return "catering-manager/login";
    }
}

