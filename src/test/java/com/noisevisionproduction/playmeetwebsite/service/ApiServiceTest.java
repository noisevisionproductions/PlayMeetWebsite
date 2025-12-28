package com.noisevisionproduction.playmeetwebsite.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSports() {
        List<String> sports = apiService.getAllSports();

        assertNotNull(sports);
        assertEquals(12, sports.size());
    }

    @Test
    void getCitiesInPoland() {
        List<String> cities = apiService.getCitiesInPoland();

        assertNotNull(cities);
        assertEquals(302, cities.size());
    }

    @Test
    void getSkillLevel() {
        Map<String, String> skillLevel = apiService.getSkillLevel();

        assertNotNull(skillLevel);
        assertEquals(10, skillLevel.size());
    }
}