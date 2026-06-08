package org.example.grandaura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for Event Coordinator authentication pages
 */
@Controller
public class EventCoordinatorAuthController {

    /**
     * Event Coordinator Login Page
     */
    @GetMapping("/event-coordinator/login")
    public String login() {
        return "event-coordinator/login";
    }
}

