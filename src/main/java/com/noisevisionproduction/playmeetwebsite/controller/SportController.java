package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api")
public class SportController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/sports")
    public List<String> getSports() {
        return apiService.getAllSports();
    }
}
