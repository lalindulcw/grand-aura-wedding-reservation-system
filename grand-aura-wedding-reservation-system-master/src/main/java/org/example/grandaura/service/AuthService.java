package org.example.grandaura.service;

import org.example.grandaura.entity.UserAccount;
import org.example.grandaura.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserAccountRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount ua = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(ua.getEmail())
                .password(ua.getPasswordHash())
                .roles("USER")
                .disabled(!ua.isEnabled())
                .build();
    }

    public UserAccount register(String email, String rawPassword) {
        UserAccount ua = new UserAccount();
        ua.setEmail(email);
        ua.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepo.save(ua);
    }
}


