package org.example.grandaura.service;

import org.example.grandaura.entity.CateringManager;
import org.example.grandaura.repository.CateringManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("cateringManagerAuthService")
public class CateringManagerAuthService implements UserDetailsService {

    private final CateringManagerRepository cateringManagerRepository;

    @Autowired
    public CateringManagerAuthService(CateringManagerRepository cateringManagerRepository) {
        this.cateringManagerRepository = cateringManagerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CateringManager cateringManager = cateringManagerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Catering Manager not found"));

        return User.withUsername(cateringManager.getEmail())
                .password(cateringManager.getPasswordHash())
                .roles("CATERING_MANAGER")
                .disabled(!cateringManager.isEnabled())
                .build();
    }
}

