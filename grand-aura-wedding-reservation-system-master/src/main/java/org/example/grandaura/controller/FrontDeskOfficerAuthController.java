package org.example.grandaura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Front Desk Officer authentication
 * Handles login page rendering
 */
@Controller
@RequestMapping("/front-desk")
public class FrontDeskOfficerAuthController {

    @GetMapping("/login")
    public String login() {
        return "front-desk/login";
    }
}

