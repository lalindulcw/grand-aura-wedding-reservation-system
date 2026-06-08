package org.example.grandaura.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemAdministratorAuthController {

    @GetMapping("/system-admin/login")
    public String showSystemAdministratorLoginPage() {
        return "system-admin/login";
    }
}
