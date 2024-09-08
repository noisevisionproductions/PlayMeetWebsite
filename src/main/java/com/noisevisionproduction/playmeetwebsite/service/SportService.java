package com.noisevisionproduction.playmeetwebsite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SportService {

    private final List<String> sports;

    public SportService() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = new ClassPathResource("json/sport_names.json").getInputStream();

        sports = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }

    public List<String> getAllSports() {
        return sports;
    }
}
