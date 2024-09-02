package com.noisevisionproduction.playmeetwebsite.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirebaseUserInitializeTest {

    @Mock
    private FirebaseAuth firebaseAuthMocked;

    @InjectMocks
    private FirebaseUserInitialize firebaseUserInitializeTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void verifyTokenTest() throws Exception {
        String idToken = "idTokenTest";
        FirebaseToken firebaseTokenMocked = mock(FirebaseToken.class);

        when(firebaseAuthMocked.verifyIdToken(idToken)).thenReturn(firebaseTokenMocked);

        FirebaseToken result = firebaseUserInitializeTest.verifyToken(idToken);

        assertNotNull(result);
        assertEquals(firebaseTokenMocked, result);

        verify(firebaseAuthMocked).verifyIdToken(idToken);
    }
}