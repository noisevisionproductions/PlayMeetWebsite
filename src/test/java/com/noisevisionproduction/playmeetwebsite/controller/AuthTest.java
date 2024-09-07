package com.noisevisionproduction.playmeetwebsite.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.service.FirebaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthTest {

    @Mock
    private FirebaseAuth firebaseAuthMocked;

    @InjectMocks
    private FirebaseService firebaseServiceTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void verifyTokenTest() throws Exception {
        String idToken = "idTokenTest";
        FirebaseToken firebaseTokenMocked = mock(FirebaseToken.class);

        when(firebaseAuthMocked.verifyIdToken(idToken)).thenReturn(firebaseTokenMocked);

        FirebaseToken result = firebaseServiceTest.verifyToken(idToken);

        assertNotNull(result);
        assertEquals(firebaseTokenMocked, result);

        verify(firebaseAuthMocked).verifyIdToken(idToken);
    }
}