package com.noisevisionproduction.playmeetwebsite.controller;

import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.FirebaseService;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /*public ResponseEntity<String> registerUser(@RequestParam("email") String email,
                                               @RequestParam("nickName") String nickName,
                                               @RequestParam("password") String password,
                                               @RequestParam("confirmPassword") String confirmPassword,
                                               @RequestParam("location") String location,
                                               @RequestParam("age") String age,
                                               @RequestParam("gender") String gender) {

        ResponseEntity<String> validationResponse = validateRegistrationFields(email, password, confirmPassword, nickName, age, gender);

        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);
            UserRecord userRecord = firebaseService.createUser(createRequest);
            String uid = userRecord.getUid();

            String encryptedAge = encryptionService.encrypt(age);
            String encryptedGender = encryptionService.encrypt(gender);
            String encryptedLocation = encryptionService.encrypt(location);

            UserModel userModel = new UserModel(uid, nickName, encryptedAge, encryptedGender, encryptedLocation);
            userService.saveUser(userModel);

            return ResponseEntity.ok("Konto utworzone pomyślnie.");
        } catch (FirebaseAuthException e) {
            if (e.getMessage().contains("EMAIL_EXISTS")) {
                return ResponseEntity.badRequest().body("Adres email jest już zarejestrowany.");
            }
            logError("Registration error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas rejestracji.");
        } catch (Exception e) {
            logError("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nieoczekiwany błąd.");
        }
    }*/

    /*private ResponseEntity<String> validateRegistrationFields(String email, String password, String confirmPassword,
                                                              String nickName, String age, String gender) {

        if (!validationService.isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Niepoprawny adres e-mail.");
        }

        if (!validationService.arePasswordsTheSame(password, confirmPassword)) {
            return ResponseEntity.badRequest().body("Hasła do siebie nie pasują.");
        }

        if (!validationService.isValidPassword(password)) {
            return ResponseEntity.badRequest().body("Hasło musi mieć co najmniej 8 znaków i zawierać litery oraz cyfry.");
        }

        try {
            validationService.validateNicknameFormat(nickName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        try {
            if (!validationService.isNicknameAvailable(nickName, nicknameValidationService)) {
                return ResponseEntity.badRequest().body("Nazwa użytkownika jest zajęta.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas sprawdzania dostępności nazwy użytkownika.");
        }

        if (!validationService.isValidAge(age)) {
            return ResponseEntity.badRequest().body("Wiek musi być między 18 a 100.");
        }

        if (!validationService.isValidGender(gender)) {
            return ResponseEntity.badRequest().body("Wybierz płeć z listy.");
        }
        return null;
    }*/
}
