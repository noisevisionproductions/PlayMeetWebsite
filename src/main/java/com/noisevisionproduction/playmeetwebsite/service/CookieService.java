package com.noisevisionproduction.playmeetwebsite.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private static final String LOGIN_COOKIE = "userId";

    public void addLoginCookie(HttpServletResponse response, String userId) {
        Cookie cookie = new Cookie(LOGIN_COOKIE, userId);
        cookie.setHttpOnly(true);
/*
        cookie.setSecure(true);
*/
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1 week
        response.addCookie(cookie);
    }

    public void clearLoginCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(LOGIN_COOKIE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public String getLoginStatusCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LOGIN_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
