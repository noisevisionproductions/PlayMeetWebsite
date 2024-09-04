package com.noisevisionproduction.playmeetwebsite.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/register")
    public String register() {
        return "register_page";
    }

    @GetMapping("/login")
    public String login() {
        return "login_page";
    }
}
