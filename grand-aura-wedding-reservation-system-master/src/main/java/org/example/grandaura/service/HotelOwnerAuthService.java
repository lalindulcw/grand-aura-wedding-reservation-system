package org.example.grandaura.service;

import org.example.grandaura.entity.HotelOwner;
import org.example.grandaura.repository.HotelOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Authentication service for Hotel Owners
 * Handles Hotel Owner login and role assignment
 */
@Service("hotelOwnerAuthService")
public class HotelOwnerAuthService implements UserDetailsService {

    private final HotelOwnerRepository hotelOwnerRepository;

    @Autowired
    public HotelOwnerAuthService(HotelOwnerRepository hotelOwnerRepository) {
        this.hotelOwnerRepository = hotelOwnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HotelOwner hotelOwner = hotelOwnerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Hotel Owner not found"));
        
        return User.withUsername(hotelOwner.getEmail())
                .password(hotelOwner.getPasswordHash())
                .roles("HOTEL_OWNER")
                .disabled(!hotelOwner.isEnabled())
                .build();
    }
}
