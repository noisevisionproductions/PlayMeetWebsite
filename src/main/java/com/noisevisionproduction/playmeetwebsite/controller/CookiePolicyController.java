package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cookies")
public class CookiePolicyController {

    private final CookieService cookieService;

    @Autowired
    public CookiePolicyController(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @PostMapping("/accept-cookies")
    public ResponseEntity<Void> acceptCookiePolicy(HttpServletResponse response) {
        cookieService.addCookiePolicyAcceptedCookie(response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/cookies-status")
    public ResponseEntity<Map<String, Boolean>> checkCookiePolicy(HttpServletRequest request) {
        boolean isAccepted = cookieService.isCookiePolicyAccepted(request);
        Map<String, Boolean> response = new HashMap<>();
        response.put("cookiePolicyAccepted", isAccepted);
        return ResponseEntity.ok(response);
    }
}
