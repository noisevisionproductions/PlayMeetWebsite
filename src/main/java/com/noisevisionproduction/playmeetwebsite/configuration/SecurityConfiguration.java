package com.noisevisionproduction.playmeetwebsite.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/posts", "/posts/api", "/faq", "/privacy_policy", "/user_account", "/create-post").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/403", "/user_account/{userId}").permitAll()
                        .requestMatchers("/api/config/firebase", "/auth/verifyToken", "/auth/perform_login", "/auth/user/session", "/auth/perform_logout").permitAll()
                        .requestMatchers("/cookies/accept-cookies", "/cookies/cookies-status").permitAll()
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
