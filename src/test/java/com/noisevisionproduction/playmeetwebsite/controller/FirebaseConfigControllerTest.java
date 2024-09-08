package com.noisevisionproduction.playmeetwebsite.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "firebase.apiKey=testApiKey",
        "firebase.authDomain=testAuthDomain",
        "firebase.projectId=testProjectId",
        "firebase.storageBucket=testStorageBucket",
        "firebase.messagingSenderId=testMessagingSenderId",
        "firebase.appId=testAppId"
})
class FirebaseConfigControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testGetFirebaseConfig() {
        String url = "http://localhost:" + port + "/api/config/firebase";
        ResponseEntity<Map<String, String>> responseEntity =
                testRestTemplate.exchange(url, org.springframework.http.HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, String> firebaseConfig = responseEntity.getBody();
        assertNotNull(firebaseConfig);

        assertEquals("testApiKey", firebaseConfig.get("apiKey"));
        assertEquals("testAuthDomain", firebaseConfig.get("authDomain"));
        assertEquals("testProjectId", firebaseConfig.get("projectId"));
        assertEquals("testStorageBucket", firebaseConfig.get("storageBucket"));
        assertEquals("testMessagingSenderId", firebaseConfig.get("messagingSenderId"));
        assertEquals("testAppId", firebaseConfig.get("appId"));
    }
}