package com.example.event_planning_dashboard.config;

import com.example.event_planning_dashboard.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Temporarily disable CSRF for debugging
                .headers(headers -> headers.frameOptions().disable())
                .authorizeRequests(authz -> authz
                        .antMatchers("/login").permitAll()
                        .antMatchers("/test-users").permitAll()
                        .antMatchers("/test").permitAll()
                        .antMatchers("/cleanup-database").permitAll()
                        .antMatchers("/cleanup").permitAll()
                        .antMatchers("/test-crud").permitAll()
                        .antMatchers("/test-persistence").permitAll()
                        .antMatchers("/test-wedding-update").permitAll()
                        .antMatchers("/test-form-submission").permitAll()
                        .antMatchers("/test-auth").permitAll()
                        .antMatchers("/test-login").permitAll()
                        .antMatchers("/debug-weddings").permitAll()
                        .antMatchers("/cleanup-orphaned-records").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/css/**").permitAll()
                        .antMatchers("/js/**").permitAll()
                        .antMatchers("/static/**").permitAll()
                        .antMatchers("/").permitAll()
                        .antMatchers("/dashboard").hasRole("EVENT_COORDINATOR")
                        .antMatchers("/event-dashboard.html").hasRole("EVENT_COORDINATOR")
                        .antMatchers("/event-planning").hasRole("EVENT_COORDINATOR")
                        .antMatchers("/coordinator/dashboard").hasRole("EVENT_COORDINATOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/coordinator/dashboard", true)  // Redirect to dashboard on success
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .rememberMe(remember -> remember.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/login?access-denied=true"))
                .userDetailsService(userDetailsService);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // For testing with plain text passwords - NOT for production use
        return new org.springframework.security.crypto.password.PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }


}
