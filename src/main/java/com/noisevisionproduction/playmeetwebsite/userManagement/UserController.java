package com.noisevisionproduction.playmeetwebsite.userManagement;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends LogsPrint {

    @PostMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String idToken = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            // If needed I can return more user info
            return ResponseEntity.ok(uid);
        } catch (Exception e) {
            logError("Invalid token", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }
    }
}
