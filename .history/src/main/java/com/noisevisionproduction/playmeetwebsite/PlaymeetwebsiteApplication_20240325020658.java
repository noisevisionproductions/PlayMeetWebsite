package com.noisevisionproduction.playmeetwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Main class in Spring Boot application.
 * 
 * @SpringBootApplication is special form @Configuration annotation, which
 *                        automatically configure
 *                        and scan components in current package.
 *                        In this sittuation it's exlcuding configuration for
 *                        security, so on the app start it won't ask for admin
 *                        credentials.
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class
})
public class PlaymeetwebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaymeetwebsiteApplication.class, args);
    }

}
