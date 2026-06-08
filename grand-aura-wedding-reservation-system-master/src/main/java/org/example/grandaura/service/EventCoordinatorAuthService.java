package org.example.grandaura.service;

import org.example.grandaura.entity.EventCoordinator;
import org.example.grandaura.repository.EventCoordinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Authentication service for Event Coordinators
 * Handles Event Coordinator login and role assignment
 */
@Service("eventCoordinatorAuthService")
public class EventCoordinatorAuthService implements UserDetailsService {

    private final EventCoordinatorRepository eventCoordinatorRepository;

    @Autowired
    public EventCoordinatorAuthService(EventCoordinatorRepository eventCoordinatorRepository) {
        this.eventCoordinatorRepository = eventCoordinatorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EventCoordinator eventCoordinator = eventCoordinatorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Event Coordinator not found"));
        
        return User.withUsername(eventCoordinator.getEmail())
                .password(eventCoordinator.getPasswordHash())
                .roles("EVENT_COORDINATOR")
                .disabled(!eventCoordinator.isEnabled())
                .build();
    }
}

