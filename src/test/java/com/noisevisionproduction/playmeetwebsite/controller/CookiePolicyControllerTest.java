package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.CookieService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CookiePolicyControllerTest {

    @Mock
    private CookieService cookieService;

    @InjectMocks
    private CookiePolicyController cookiePolicyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptCookiePolicy() {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        ResponseEntity<Void> responseEntity = cookiePolicyController.acceptCookiePolicy(mockHttpServletResponse);

        verify(cookieService).addCookiePolicyAcceptedCookie(mockHttpServletResponse);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testCheckCookiePolicy() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        when(cookieService.isCookiePolicyAccepted(mockHttpServletRequest)).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> responseEntity = cookiePolicyController.checkCookiePolicy(mockHttpServletRequest);
        Map<String, Boolean> response = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        if (response != null) {
            assertTrue(response.get("cookiePolicyAccepted"));
        }
    }
}