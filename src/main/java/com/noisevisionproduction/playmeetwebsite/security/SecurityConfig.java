package com.noisevisionproduction.playmeetwebsite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/posts", "/posts/api", "/faq", "/privacy_policy").permitAll()
                        .requestMatchers("/api/config/firebase").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/403").permitAll()
                        .requestMatchers("/verifyToken").permitAll()  // Permit access to /verifyToken
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403"));

        return httpSecurity.build();
    }
}
