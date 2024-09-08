package com.noisevisionproduction.playmeetwebsite.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieServiceTest {

    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        cookieService = new CookieService();
    }

    @Test
    public void testAddLoginCookie() {
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);

        String userId = "userId";
        cookieService.addLoginCookie(httpServletResponse, userId);

        verify(httpServletResponse).addCookie(argumentCaptor.capture());
        Cookie cookie = argumentCaptor.getValue();

        assertEquals("userId", cookie.getName());
        assertEquals(userId, cookie.getValue());
/*
        assertTrue(cookie.getSecure());
*/
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertEquals(7 * 24 * 60 * 60, cookie.getMaxAge());
    }

    @Test
    public void testClearLoginCookie() {
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);

        cookieService.clearLoginCookie(httpServletResponse);

        verify(httpServletResponse).addCookie(argumentCaptor.capture());
        Cookie cookie = argumentCaptor.getValue();

        assertEquals("userId", cookie.getName());
        assertNull(cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    public void testGetLoginStatusCookie() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        Cookie[] cookies = {new Cookie("userId", "user123")};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        String userId = cookieService.getLoginStatusCookie(httpServletRequest);
        assertEquals("user123", userId);

        when(httpServletRequest.getCookies()).thenReturn(null);
        userId = cookieService.getLoginStatusCookie(httpServletRequest);
        assertNull(userId);
    }

    @Test
    public void testAddCookiePolicyAcceptedCookie() {
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        ArgumentCaptor<Cookie> argumentCaptor = ArgumentCaptor.forClass(Cookie.class);

        cookieService.addCookiePolicyAcceptedCookie(httpServletResponse);

        verify(httpServletResponse).addCookie(argumentCaptor.capture());
        Cookie cookie = argumentCaptor.getValue();

        assertEquals("cookiePolicyAccepted", cookie.getName());
        assertEquals("true", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertEquals(365 * 24 * 60 * 60, cookie.getMaxAge());
    }

    @Test
    public void testIsCookiePolicyAccepted() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        Cookie[] cookies = {new Cookie("cookiePolicyAccepted", "true")};
        when(httpServletRequest.getCookies()).thenReturn(cookies);
        boolean isAccepted = cookieService.isCookiePolicyAccepted(httpServletRequest);
        assertTrue(isAccepted);

        when(httpServletRequest.getCookies()).thenReturn(null);
        isAccepted = cookieService.isCookiePolicyAccepted(httpServletRequest);
        assertFalse(isAccepted);
    }
}