package com.noisevisionproduction.playmeetwebsite.configuration;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// Test checks if Firebase and its services works properly
@SpringJUnitConfig // Used this annotation since it is more lightweight compared to @SpringBootTest
@TestPropertySource(locations = "classpath:keys.properties")
public class FirebaseConfigurationTest {

    @Value("${firebase.config.path}")
    private String configPath;

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Mock
    private FirebaseApp mockFirebaseApp;

    @Mock
    private FirebaseAuth firebaseAuthMocked;

    @Mock
    private Firestore firestoreMocked;

    @Mock
    private FirebaseDatabase firebaseDatabaseMocked;

    private FirebaseConfiguration firebaseConfiguration;

    // loading instance of a FirebaseInitialization class in order to test everything properly
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        firebaseConfiguration = new FirebaseConfiguration(configPath);
    }

    @Test
    public void testFirebaseAppInitialization() throws Exception {
        try (MockedStatic<FirebaseApp> firebaseAppMockedStatic = mockStatic(FirebaseApp.class)) {
            firebaseAppMockedStatic.when(FirebaseApp::getApps).thenReturn(Collections.emptyList());
            firebaseAppMockedStatic.when(() -> FirebaseApp.initializeApp(any(FirebaseOptions.class)))
                    .thenReturn(mockFirebaseApp);

            FirebaseApp firebaseApp = firebaseConfiguration.firebaseApp();

            assertNotNull(firebaseApp);

            firebaseAppMockedStatic.verify(FirebaseApp::getApps);
            firebaseAppMockedStatic.verify(() -> FirebaseApp.initializeApp(any(FirebaseOptions.class)));
        }
    }

    @Test
    public void testFirebaseAppInitializationWhenAppsExist() throws Exception {
        try (MockedStatic<FirebaseApp> firebaseAppMockedStatic = mockStatic(FirebaseApp.class)) {
            FirebaseApp existingFirebaseApp = mock(FirebaseApp.class);
            firebaseAppMockedStatic.when(FirebaseApp::getApps).thenReturn(Collections.singletonList(existingFirebaseApp));
            firebaseAppMockedStatic.when((MockedStatic.Verification) FirebaseApp.getInstance()).thenReturn(existingFirebaseApp);

            FirebaseApp firebaseApp = firebaseConfiguration.firebaseApp();

            assertNotNull(firebaseApp);
            assertEquals(existingFirebaseApp, firebaseApp);
            firebaseAppMockedStatic.verify(FirebaseApp::getApps);
            firebaseAppMockedStatic.verify(() -> FirebaseApp.initializeApp(any(FirebaseOptions.class)), never());
        }
    }

    @Test
    public void testFirebaseAuthInitialization() {
        try (MockedStatic<FirebaseAuth> firebaseAuthMockedStatic = mockStatic(FirebaseAuth.class)) {
            firebaseAuthMockedStatic.when(() -> FirebaseAuth.getInstance(mockFirebaseApp))
                    .thenReturn(firebaseAuthMocked);

            FirebaseAuth firebaseAuth = firebaseConfiguration.firebaseAuth(mockFirebaseApp);

            assertNotNull(firebaseAuth);
            firebaseAuthMockedStatic.verify(() -> FirebaseAuth.getInstance(mockFirebaseApp));
        }
    }

    @Test
    public void testFirestoreInitialization() {
        try (MockedStatic<FirestoreClient> firestoreClientMockedStatic = mockStatic(FirestoreClient.class)) {
            firestoreClientMockedStatic.when(FirestoreClient::getFirestore).thenReturn(firestoreMocked);

            Firestore firestore = firebaseConfiguration.firestore();

            assertNotNull(firestore);
            firestoreClientMockedStatic.verify(FirestoreClient::getFirestore);
        }
    }

    @Test
    public void testFirebaseDatabaseInitialization() {
        try (MockedStatic<FirebaseDatabase> firebaseDatabaseMockedStatic = mockStatic(FirebaseDatabase.class)) {
            firebaseDatabaseMockedStatic.when(() -> FirebaseDatabase.getInstance(mockFirebaseApp)).thenReturn(firebaseDatabaseMocked);

            FirebaseDatabase firebaseDatabase = firebaseConfiguration.firebaseDatabase(mockFirebaseApp);

            assertNotNull(firebaseDatabase);
            assertEquals(firebaseDatabaseMocked, firebaseDatabase);

            firebaseDatabaseMockedStatic.verify(() -> FirebaseDatabase.getInstance(mockFirebaseApp));
        }
    }
}
