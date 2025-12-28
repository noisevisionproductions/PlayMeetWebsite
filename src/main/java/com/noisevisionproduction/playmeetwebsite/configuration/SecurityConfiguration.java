package com.noisevisionproduction.playmeetwebsite.configuration;

import com.noisevisionproduction.playmeetwebsite.utils.CookieAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CookieAuthenticationFilter cookieAuthenticationFilter;

    public SecurityConfiguration(CookieAuthenticationFilter cookieAuthenticationFilter) {
        this.cookieAuthenticationFilter = cookieAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/posts", "/posts/api", "/faq", "/privacy_policy", "/user_account").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                        .requestMatchers("/403", "/user_account/{userId}").permitAll()
                        .requestMatchers("/api/config/firebase", "/api/posts").permitAll()
                        .requestMatchers("/auth/verifyToken", "/auth/perform_login", "/auth/user/session", "/auth/perform_logout").permitAll()
                        .requestMatchers("/cookies/accept-cookies", "/cookies/cookies-status").permitAll()
                        .requestMatchers("/user_account/**").authenticated()
                        .requestMatchers("/create-post", "/api/posts/create", "/api/posts/register-for-post", "/api/posts/unregister-from-post", "/user_account/{userId}/edit", "/user_account/{userId}/delete-avatar").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403"));
        return httpSecurity.build();
    }
}

