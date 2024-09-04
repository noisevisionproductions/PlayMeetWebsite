package com.noisevisionproduction.playmeetwebsite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/privacy_policy")
    public String privatePolicy() {
        return "privacy_policy";
    }

    @GetMapping("/faq")
    public String showDeleteAccountForm() {
        return "faq";
    }

    @GetMapping("/")
    public String landingPage() {
        return "landing_page";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
