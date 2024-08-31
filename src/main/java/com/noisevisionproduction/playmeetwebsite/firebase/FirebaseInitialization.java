package com.noisevisionproduction.playmeetwebsite.firebase;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

/**
 * Class that initialize connection with Firebase using Firebase Admin SDK.
 * It loads credentials and initializes credentials from my Firebase app.
 * It allows to connect to Firebase services like database.
 * 
 * @Service means that class is service component, which means that it is
 *          automatically detected on start and can be injected as dependency in
 *          others app components.
 * 
 * @PostConstruct is used to mark method that should be called on the start
 *                class constructor,
 *                but before returning it to the Spring container.
 */
@Service
public class FirebaseInitialization {

    @PostConstruct
    public void initialization() {
        try {
            FileInputStream serviceAccount = new FileInputStream(
                    "C:/Users/noise/Desktop/Java/Projects/PlayMeetWebsite/config-firebase.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://zagrajmy-b418d-default-rtdb.europe-west1.firebasedatabase.app/")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (IOException e) {
            System.out.println("Error connecting to firebase: " + e.getMessage());
        }
    }
}
