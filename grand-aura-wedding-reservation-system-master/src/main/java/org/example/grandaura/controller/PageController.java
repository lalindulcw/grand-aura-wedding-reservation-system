package org.example.grandaura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for static/informational pages
 */
@Controller
public class PageController {

    /**
     * Gallery page
     */
    @GetMapping("/gallery")
    public String gallery() {
        return "gallery";
    }

    /**
     * Contact page
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    /**
     * Privacy Policy page
     */
    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    /**
     * Terms of Service page
     */
    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    /**
     * Cookie Policy page
     */
    @GetMapping("/cookies")
    public String cookies() {
        return "cookies";
    }

    /**
     * Refund Policy page
     */
    @GetMapping("/refund")
    public String refund() {
        return "refund";
    }
}

