package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApiControllerTest {

    @Mock
    private ApiService apiService;

    @Mock
    private Model model;

    @InjectMocks
    private ApiController apiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSports() {
        List<String> sports = Arrays.asList("Football", "Basketball");
        when(apiService.getAllSports()).thenReturn(sports);

        apiController.getSports(model);

        verify(model).addAttribute("sports", sports);
    }

    @Test
    void getCitiesInPoland() {
        List<String> citiesInPoland = Arrays.asList("Katowice", "Warszawa");
        when(apiService.getCitiesInPoland()).thenReturn(citiesInPoland);

        apiController.getCitiesInPoland(model);

        verify(model).addAttribute("citiesInPoland", citiesInPoland);
    }

    @Test
    void getSkillLevelName() {
        Map<String, String> skillLevel = new HashMap<>();
        skillLevel.put("1", "easy");
        skillLevel.put("2", "hard");
        when(apiService.getSkillLevel()).thenReturn(skillLevel);

        apiController.getSkillLevelName(model);

        verify(model).addAttribute("skillLevelNames", skillLevel);
    }
}