package com.noisevisionproduction.playmeetwebsite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/signin", "/signup", "/posts", "/posts/api", "/faq", "/privacy_policy").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                    .anyRequest()
                    .authenticated())
            .formLogin(form -> form
                    .loginPage("/signin")
                    .permitAll())
            .logout(LogoutConfigurer::permitAll)
            .exceptionHandling(exception -> exception
                    .accessDeniedPage("/403"));

    return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
