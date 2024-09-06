package com.noisevisionproduction.playmeetwebsite.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController extends LogsPrint {

    @PostMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String authHeader, HttpSession session) {
        try {
            String idToken = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            session.setAttribute("user", uid);

            // If needed I can return more user info
            return ResponseEntity.ok(uid);
        } catch (Exception e) {
            logError("Invalid token", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }
    }

    @GetMapping("/user/session")
    public ResponseEntity<Map<String, String>> getUserSession(HttpSession session) {
        String user = (String) session.getAttribute("user");
        Map<String, String> response = new HashMap<>();
        if (user != null) {
            response.put("status", "logged_in");
            response.put("user", user);
        } else {
            response.put("status", "logged_out");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
