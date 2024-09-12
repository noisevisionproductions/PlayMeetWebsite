package com.noisevisionproduction.playmeetwebsite.utils;

import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieAuthenticationFilterTest {

    @Mock
    private CookieService cookieService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CookieAuthenticationFilter cookieAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String userId = "userId";

        when(cookieService.getLoginStatusCookie(request)).thenReturn(userId);

        cookieAuthenticationFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authenticationToken);
        assertEquals(userId, authenticationToken.getPrincipal());
        assertTrue(authenticationToken.getAuthorities().isEmpty());

        verify(filterChain).doFilter(request, response);

        SecurityContextHolder.clearContext();

        when(cookieService.getLoginStatusCookie(request)).thenReturn(null);

        cookieAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(2)).doFilter(request, response);
    }
}