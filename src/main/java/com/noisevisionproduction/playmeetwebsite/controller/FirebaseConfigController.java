package com.noisevisionproduction.playmeetwebsite.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class FirebaseConfigController {

    @Value("${firebase.apiKey}")
    private String apiKey;

    @Value("${firebase.authDomain}")
    private String authDomain;

    @Value("${firebase.projectId}")
    private String projectId;

    @Value("${firebase.storageBucket}")
    private String storageBucket;

    @Value("${firebase.messagingSenderId}")
    private String messagingSenderId;

    @Value("${firebase.appId}")
    private String appId;

    @GetMapping("/firebase")
    public ResponseEntity<Map<String, String>> getFirebaseConfig() {
        Map<String, String> firebaseConfig = new HashMap<>();
        firebaseConfig.put("apiKey", apiKey);
        firebaseConfig.put("authDomain", authDomain);
        firebaseConfig.put("projectId", projectId);
        firebaseConfig.put("storageBucket", storageBucket);
        firebaseConfig.put("messagingSenderId", messagingSenderId);
        firebaseConfig.put("appId", appId);
        return ResponseEntity.ok(firebaseConfig);
    }
}
