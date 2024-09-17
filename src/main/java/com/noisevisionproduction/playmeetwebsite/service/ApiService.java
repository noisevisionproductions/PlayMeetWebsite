package com.noisevisionproduction.playmeetwebsite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final List<String> sports;
    private final List<String> citiesInPoland;
    private final Map<String, String> skillLevel;
    private static final String API_URL = "https://api.alternative.me/fng/?limit=1";

    @Autowired
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

    public String getCryptoFearAndGreedIndex() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(API_URL, String.class);

        if (response != null) {
            JSONObject jsonObject = new JSONObject(response);

            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);

            String value = data.getString("value");
            String classification = data.getString("value_classification");

            return "Aktualny index Bitcoin Fear & Gread: " + value + " (" + classification + ")";
        }
        return null;
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
