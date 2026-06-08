package org.example.grandaura.service;

import org.example.grandaura.entity.SystemAdministrator;
import org.example.grandaura.repository.SystemAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("systemAdministratorAuthService")
public class SystemAdministratorAuthService implements UserDetailsService {

    private final SystemAdministratorRepository systemAdministratorRepository;

    @Autowired
    public SystemAdministratorAuthService(SystemAdministratorRepository systemAdministratorRepository) {
        this.systemAdministratorRepository = systemAdministratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemAdministrator systemAdministrator = systemAdministratorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("System Administrator not found"));

        return User.withUsername(systemAdministrator.getEmail())
                .password(systemAdministrator.getPasswordHash())
                .roles("SYSTEM_ADMIN")
                .disabled(!systemAdministrator.isEnabled())
                .build();
    }
}
