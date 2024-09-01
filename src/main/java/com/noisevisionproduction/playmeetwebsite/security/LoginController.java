package com.noisevisionproduction.playmeetwebsite.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
/*
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return "redirect:/home";
    }*/
}
