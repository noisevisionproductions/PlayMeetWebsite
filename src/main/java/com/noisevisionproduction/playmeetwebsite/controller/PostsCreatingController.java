package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostCreatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostsCreatingController {

    @Autowired
    private PostCreatingService postCreatingService;

    @Autowired
    private CookieService cookieService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@ModelAttribute PostRequest postRequest, HttpServletRequest httpServletRequest) {
        String userId = cookieService.getLoginStatusCookie(httpServletRequest);
        if (userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Użytkownik nie zalogowany");
        }
        String postId = postCreatingService.createPost(postRequest, userId);
        return ResponseEntity.ok("Post utworzony" + postId);
    }
}
