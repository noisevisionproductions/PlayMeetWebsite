package com.noisevisionproduction.playmeetwebsite.userManagement;

import com.google.firebase.auth.FirebaseToken;
import com.noisevisionproduction.playmeetwebsite.firebase.FirebaseUserInitialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private FirebaseUserInitialize firebaseUserInitialize;


    @GetMapping("/signin")
    public String showSignInPage() {
        return "signin";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String idToken) {
        try {
            FirebaseToken firebaseToken = firebaseUserInitialize.verifyToken(idToken);
            String uid = firebaseToken.getUid();

            return new RedirectView("/");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new RedirectView("/signin?error=true");
        }
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView signUp(@RequestParam String email, @RequestParam String password) {
        return new RedirectView("/signin");
    }
}
