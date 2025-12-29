package com.ishant.journalApp.config;

import com.ishant.journalApp.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity

public class SpringSecurity {

    private final UserDetailServiceImpl userDetailService;

    public SpringSecurity(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    // --------------------------
    // Password Encoder Bean
    // --------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --------------------------
    // Authentication Provider
    // --------------------------
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder()); // <-- attach encoder
        return authProvider;
    }

    // --------------------------
    // Authentication Manager
    // --------------------------
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(authenticationProvider()); // use provider with password encoder
        return builder.build();
    }

    // --------------------------
    // Security Filter Chain
    // --------------------------

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for Postman/testing
                .authorizeHttpRequests(auth -> auth
                        // public endpoints: no login required
                        .requestMatchers("/public/**").permitAll()

                        // protected endpoints: require login
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/journal/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // any other endpoints are also protected
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // HTTP Basic authentication

        return http.build();
    }
}

