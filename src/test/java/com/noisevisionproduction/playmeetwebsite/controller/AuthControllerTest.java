package com.noisevisionproduction.playmeetwebsite.controller;

import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.FirebaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private CookieService cookieService;

    @Mock
    private FirebaseService firebaseService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifyValidToken() throws Exception {
        String idToken = "idToken";
        String userId = "userId";
        FirebaseToken firebaseToken = mock(FirebaseToken.class);

        when(firebaseService.verifyToken(idToken)).thenReturn(firebaseToken);
        when(firebaseToken.getUid()).thenReturn(userId);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = authController.verifyToken("Bearer " + idToken, response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userId, responseEntity.getBody());
        verify(cookieService).addLoginCookie(response, userId);
    }

    @Test
    public void testVerifyTokenInvalid() throws Exception {
        String idToken = "invalidToken";

        when(firebaseService.verifyToken(idToken)).thenThrow(new RuntimeException("Invalid token"));

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        ResponseEntity<String> responseEntity = authController.verifyToken("Bearer " + idToken, mockHttpServletResponse);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Invalid token", responseEntity.getBody());
    }

    @Test
    public void testLogoutClearsCookie() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        ResponseEntity<Void> responseEntity = authController.logout(mockHttpServletResponse);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(cookieService).clearLoginCookie(mockHttpServletResponse);
    }

    @Test
    public void testUserSessionLoggedIn() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        String userId = "userId";

        when(cookieService.getLoginStatusCookie(mockHttpServletRequest)).thenReturn(userId);

        ResponseEntity<Map<String, String>> responseEntity = authController.getUserSession(mockHttpServletRequest);
        Map<String, String> response = responseEntity.getBody();

        if (response != null) {
            assertEquals("logged_in", response.get("status"));
            assertEquals(userId, response.get("user"));
        }
    }

    @Test
    public void testUserSessionLoggedOut() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        when(cookieService.getLoginStatusCookie(mockHttpServletRequest)).thenReturn(null);

        ResponseEntity<Map<String, String>> responseEntity = authController.getUserSession(mockHttpServletRequest);
        Map<String, String> response = responseEntity.getBody();

        if (response != null) {
            assertEquals("logged_out", response.get("status"));
            assertNull(response.get("user"));
        }
    }
}