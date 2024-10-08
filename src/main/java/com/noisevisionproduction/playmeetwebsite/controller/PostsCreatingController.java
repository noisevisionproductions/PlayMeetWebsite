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

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostsCreatingController {

    private final PostCreatingService postCreatingService;
    private final CookieService cookieService;

    @Autowired
    public PostsCreatingController(PostCreatingService postCreatingService, CookieService cookieService) {
        this.postCreatingService = postCreatingService;
        this.cookieService = cookieService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@ModelAttribute PostRequest postRequest, HttpServletRequest httpServletRequest) {
        String userId = cookieService.getLoginStatusCookie(httpServletRequest);
        String postId = postCreatingService.createPost(postRequest, userId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/?postId=" + postId))
                .build();
    }

}