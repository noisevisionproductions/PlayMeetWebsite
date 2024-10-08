package com.noisevisionproduction.playmeetwebsite.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class that initialize connection with Firebase using Firebase Admin SDK.
 * It loads credentials and initializes credentials from my Firebase app.
 * It allows to connect to Firebase services like database.
 *
 * @Service means that class is service component, which means that it is
 * automatically detected on start and can be injected as dependency in
 * others app components.
 * @PostConstruct is used to mark method that should be called on the start
 * class constructor,
 * but before returning it to the Spring container.
 */
@Configuration
public class FirebaseConfiguration {

    private final String databaseUrl;

    public FirebaseConfiguration(@Value("${firebase.database.url}") String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = new ClassPathResource("config-firebase.json").getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }

    @Bean
    public FirebaseDatabase firebaseDatabase(FirebaseApp firebaseApp) {
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}
