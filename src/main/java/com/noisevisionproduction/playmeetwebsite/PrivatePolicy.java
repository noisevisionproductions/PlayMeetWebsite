package com.noisevisionproduction.playmeetwebsite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivatePolicy {
    @GetMapping("/privacy_policy")
    public String privatePolicy() {
        return "privacy_policy";
    }

    @GetMapping("/faq")
    public String showDeleteAccountForm() {
        return "faq";
    }

}
