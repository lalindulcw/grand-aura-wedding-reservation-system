package org.example.grandaura.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.grandaura.repository.UserAccountRepository;
import org.example.grandaura.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserAccountRepository userRepo;

    @Autowired
    public AuthController(AuthService authService, UserAccountRepository userRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam("email") @Email @NotBlank String email,
                             @RequestParam("password") @NotBlank @Size(min = 8) String password,
                             RedirectAttributes redirectAttributes, Model model) {
        if (userRepo.existsByEmail(email)) {
            model.addAttribute("message", "Email already registered");
            return "register";
        }
        authService.register(email, password);
        redirectAttributes.addFlashAttribute("message", "Registration successful. Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/hotel-owner/login")
    public String showHotelOwnerLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "hotel-owner/login";
    }
}


