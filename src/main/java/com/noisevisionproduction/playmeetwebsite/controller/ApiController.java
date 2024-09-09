package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getSports(Model model) {
        List<String> sports = apiService.getAllSports();
        model.addAttribute("sports", sports);
    }

    public void getCitiesInPoland(Model model) {
        List<String> citiesInPoland = apiService.getCitiesInPoland();
        model.addAttribute("citiesInPoland", citiesInPoland);
    }

    public void getSkillLevelName(Model model) {
        Map<String, String> skillLevel = apiService.getSkillLevel();
        model.addAttribute("skillLevelNames", skillLevel);
    }

    @PostMapping("/fear-greed-index")
    public String getFearGreedIndex(Model model) {
        String fearApi = apiService.getCryptoFearAndGreedIndex();
        model.addAttribute("fearGreedIndex", fearApi);
        return "login_page";
    }
}
