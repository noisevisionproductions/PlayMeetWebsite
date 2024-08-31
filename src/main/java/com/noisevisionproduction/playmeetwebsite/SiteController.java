package com.noisevisionproduction.playmeetwebsite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SiteController {

    @GetMapping("/")
    public String home() {
        return "landing_page";
    }

}
