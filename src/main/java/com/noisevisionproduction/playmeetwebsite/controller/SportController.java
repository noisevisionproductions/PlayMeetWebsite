package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SportController {

    @Autowired
    private SportService sportService;

    @GetMapping("/sports")
    public List<String> getSports() {
        return sportService.getAllSports();
    }
}
