package com.noisevisionproduction.playmeetwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {

    @Autowired
    private ApiController apiController;

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

    @GetMapping("/login")
    public String login() {
        return "login_page";
    }

    @GetMapping("/create-post")
    public String postCreating(Model model) {
        apiController.getSports(model);
        apiController.getCitiesInPoland(model);
        apiController.getSkillLevelName(model);
        return "post_creating";
    }
}
