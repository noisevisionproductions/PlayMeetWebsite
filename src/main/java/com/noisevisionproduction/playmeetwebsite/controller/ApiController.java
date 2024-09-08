package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ApiController {

    @Autowired
    private ApiService apiService;

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
        List<String> skillLevelNames = new ArrayList<>(skillLevel.values());
        model.addAttribute("skillLevelNames", skillLevelNames);
    }
}
