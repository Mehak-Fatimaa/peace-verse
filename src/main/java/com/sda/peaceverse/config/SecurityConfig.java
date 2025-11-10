package com.sda.peaceverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for frontend fetch requests
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/verse.html",
                                "/verseDisplay.html",
                                "/css/**",
                                "/js/**",
                                "/api/analyzeMood",
                                "/api/userVerses",
                                "/api/saveVerse",
//                                "/api/**",
                                "/auth/**"
                        ).permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // Disable Spring's default login form

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
