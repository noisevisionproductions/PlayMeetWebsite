package com.noisevisionproduction.playmeetwebsite.controller;

import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.FirebaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController extends LogsPrint {

    private final FirebaseService firebaseService;
    private final CookieService cookieService;

    @Autowired
    public AuthController(FirebaseService firebaseService, CookieService cookieService) {
        this.firebaseService = firebaseService;
        this.cookieService = cookieService;
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String authHeader, HttpServletResponse response) {
        try {
            String idToken = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
            String userId = decodedToken.getUid();

            cookieService.addLoginCookie(response, userId);

            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            logError("Invalid token", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }
    }

    @PostMapping("/perform_logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.clearLoginCookie(response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/session")
    public ResponseEntity<Map<String, String>> getUserSession(HttpServletRequest request) {
        String user = cookieService.getLoginStatusCookie(request);
        Map<String, String> response = new HashMap<>();
        if (user != null) {
            response.put("status", "logged_in");
            response.put("user", user);
        } else {
            response.put("status", "logged_out");
        }
        return ResponseEntity.ok(response);
    }
}
