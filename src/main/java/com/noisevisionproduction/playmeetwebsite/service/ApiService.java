package com.noisevisionproduction.playmeetwebsite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final List<String> sports;
    private final List<String> citiesInPoland;
    private final Map<String, String> skillLevel;

    public ApiService() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStreamForSportNames = new ClassPathResource("json/sport_names.json").getInputStream();
        InputStream inputStreamForCitiesInPoland = new ClassPathResource("json/cities_in_poland.json").getInputStream();
        InputStream inputStreamForSkillLevel = new ClassPathResource("json/skill_level.json").getInputStream();

        sports = objectMapper.readValue(inputStreamForSportNames, new TypeReference<>() {
        });
        citiesInPoland = objectMapper.readValue(inputStreamForCitiesInPoland, new TypeReference<>() {
        });
        skillLevel = objectMapper.readValue(inputStreamForSkillLevel, new TypeReference<>() {
        });
    }

    public List<String> getAllSports() {
        return sports;
    }

    public List<String> getCitiesInPoland() {
        return citiesInPoland;
    }

    public Map<String, String> getSkillLevel() {
        return skillLevel;
    }
}
