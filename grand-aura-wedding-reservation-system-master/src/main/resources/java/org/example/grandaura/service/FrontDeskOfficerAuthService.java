package org.example.grandaura.service;

import org.example.grandaura.entity.FrontDeskOfficer;
import org.example.grandaura.repository.FrontDeskOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Authentication service for Front Desk Officer
 * Implements UserDetailsService for Spring Security integration
 */
@Service
public class FrontDeskOfficerAuthService implements UserDetailsService {

    private final FrontDeskOfficerRepository frontDeskOfficerRepository;

    @Autowired
    public FrontDeskOfficerAuthService(FrontDeskOfficerRepository frontDeskOfficerRepository) {
        this.frontDeskOfficerRepository = frontDeskOfficerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        FrontDeskOfficer officer = frontDeskOfficerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Front Desk Officer not found with email: " + email));

        return new User(
                officer.getEmail(),
                officer.getPasswordHash(),
                officer.getEnabled(),
                true, true, true,
                getAuthorities()
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_FRONT_DESK"));
    }
}

