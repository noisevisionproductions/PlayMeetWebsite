package com.noisevisionproduction.playmeetwebsite.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private static final String USER_ID = "userId";
    private static final String POLICY_COOKIES = "cookiePolicyAccepted";

    public void addLoginCookie(HttpServletResponse response, String userId) {
        Cookie cookie = new Cookie(USER_ID, userId);
        cookie.setHttpOnly(true);
/*
        cookie.setSecure(true);
*/
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1 week
        response.addCookie(cookie);
    }

    public void clearLoginCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(USER_ID, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public String getLoginStatusCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_ID.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void addCookiePolicyAcceptedCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(POLICY_COOKIES, "true");
        cookie.setPath("/");
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1 year
        response.addCookie(cookie);
    }

    public boolean isCookiePolicyAccepted(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (POLICY_COOKIES.equals(cookie.getName()) && "true".equals(cookie.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
