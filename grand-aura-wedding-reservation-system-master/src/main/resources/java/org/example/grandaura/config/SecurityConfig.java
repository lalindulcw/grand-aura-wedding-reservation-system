package org.example.grandaura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.example.grandaura.service.AuthService;
import org.example.grandaura.service.HotelOwnerAuthService;
import org.example.grandaura.service.EventCoordinatorAuthService;
import org.example.grandaura.service.SystemAdministratorAuthService;
import org.example.grandaura.service.CateringManagerAuthService;
import org.example.grandaura.service.FrontDeskOfficerAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AuthService authService) {
        return authService;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthService authService,
            HotelOwnerAuthService hotelOwnerAuthService,
            EventCoordinatorAuthService eventCoordinatorAuthService,
            SystemAdministratorAuthService systemAdministratorAuthService,
            CateringManagerAuthService cateringManagerAuthService,
            FrontDeskOfficerAuthService frontDeskOfficerAuthService,
            PasswordEncoder passwordEncoder) {
        
        // Regular users authentication provider
        DaoAuthenticationProvider userProvider = new DaoAuthenticationProvider();
        userProvider.setUserDetailsService(authService);
        userProvider.setPasswordEncoder(passwordEncoder);
        
        // Hotel owner authentication provider
        DaoAuthenticationProvider hotelOwnerProvider = new DaoAuthenticationProvider();
        hotelOwnerProvider.setUserDetailsService(hotelOwnerAuthService);
        hotelOwnerProvider.setPasswordEncoder(passwordEncoder);
        
        // Event coordinator authentication provider
        DaoAuthenticationProvider eventCoordinatorProvider = new DaoAuthenticationProvider();
        eventCoordinatorProvider.setUserDetailsService(eventCoordinatorAuthService);
        eventCoordinatorProvider.setPasswordEncoder(passwordEncoder);
        
        // System administrator authentication provider
        DaoAuthenticationProvider systemAdminProvider = new DaoAuthenticationProvider();
        systemAdminProvider.setUserDetailsService(systemAdministratorAuthService);
        systemAdminProvider.setPasswordEncoder(passwordEncoder);
        
        // Catering manager authentication provider
        DaoAuthenticationProvider cateringManagerProvider = new DaoAuthenticationProvider();
        cateringManagerProvider.setUserDetailsService(cateringManagerAuthService);
        cateringManagerProvider.setPasswordEncoder(passwordEncoder);
        
        // Front desk officer authentication provider
        DaoAuthenticationProvider frontDeskProvider = new DaoAuthenticationProvider();
        frontDeskProvider.setUserDetailsService(frontDeskOfficerAuthService);
        frontDeskProvider.setPasswordEncoder(passwordEncoder);
        
        return new ProviderManager(userProvider, hotelOwnerProvider, eventCoordinatorProvider, systemAdminProvider, cateringManagerProvider, frontDeskProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/hotel-owner/login", "/event-coordinator/login", "/system-admin/login", "/catering-manager/login", "/front-desk/login", "/css/**", "/js/**", "/img/**", "/video/**").permitAll()
                .requestMatchers("/gallery", "/contact", "/privacy", "/terms", "/cookies", "/refund").permitAll() // Public informational pages
                .requestMatchers("/bookings/**").hasAnyRole("USER", "CATERING_MANAGER", "HOTEL_OWNER", "EVENT_COORDINATOR", "SYSTEM_ADMIN", "FRONT_DESK")
                .requestMatchers("/hotel-owner/**").hasRole("HOTEL_OWNER")
                .requestMatchers("/event-coordinator/**").hasRole("EVENT_COORDINATOR")
                .requestMatchers("/system-admin/**").hasRole("SYSTEM_ADMIN")
                .requestMatchers("/catering-manager/**").hasRole("CATERING_MANAGER")
                .requestMatchers("/front-desk/**").hasRole("FRONT_DESK")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
            )
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());
        return http.build();
    }
}


