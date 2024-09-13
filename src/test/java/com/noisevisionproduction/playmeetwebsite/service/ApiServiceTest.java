package com.noisevisionproduction.playmeetwebsite.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCryptoFearAndGreedIndex() {
        String mockApiResponse = "{ 'data': [{ 'value': '50', 'value_classification': 'Neutral' }] }";
        when(restTemplate.getForObject("https://api.alternative.me/fng/?limit=1", String.class)).thenReturn(mockApiResponse);

        String result = apiService.getCryptoFearAndGreedIndex();

        assertEquals("Aktualny index Bitcoin Fear & Gread: 50 (Neutral)", result);

        when(restTemplate.getForObject("https://api.alternative.me/fng/?limit=1", String.class)).thenReturn(null);
        String resultIsNull = apiService.getCryptoFearAndGreedIndex();
        assertNull(resultIsNull);
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