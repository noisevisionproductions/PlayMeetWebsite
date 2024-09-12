package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostCreatingService;
import com.noisevisionproduction.playmeetwebsite.service.PostsRegistrationService;
import com.noisevisionproduction.playmeetwebsite.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostsCreatingController {

    private final PostsRegistrationService postsRegistrationService;
    private final PostCreatingService postCreatingService;
    private final CookieService cookieService;
    private final PostsService postsService;

    @Autowired
    public PostsCreatingController(PostsRegistrationService postsRegistrationService, PostCreatingService postCreatingService, CookieService cookieService, PostsService postsService) {
        this.postsRegistrationService = postsRegistrationService;
        this.postCreatingService = postCreatingService;
        this.cookieService = cookieService;
        this.postsService = postsService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@ModelAttribute PostRequest postRequest, HttpServletRequest httpServletRequest) {
        String userId = cookieService.getLoginStatusCookie(httpServletRequest);
        String postId = postCreatingService.createPost(postRequest, userId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/?postId=" + postId))
                .build();
    }

    @PostMapping("/register-for-post")
    @ResponseBody
    public Map<String, Object> signUserToPost(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String postId = requestBody.get("postId");
        String userId = cookieService.getLoginStatusCookie(request);
        Map<String, Object> response = new HashMap<>();
        PostModel postModel = postsService.getPostById(postId);

        if (isUserNotLoggedIn(userId, response)) return response;
        if (isUserPostOwner(postModel, userId, response)) return response;
        if (isUserAlreadyRegistered(postId, userId, response)) return response;
        if (hasUserReachedPostLimit(userId, response)) return response;
        if (isPostFull(postModel, response)) return response;

        boolean success = postsRegistrationService.registerUserForPost(postId, userId);
        return buildRegistrationResponse(success, response);
    }

    private boolean isUserNotLoggedIn(String userId, Map<String, Object> response) {
        if (userId == null) {
            response.put("success", false);
            response.put("message", "Musisz być zalogowany, aby się zapisać");
            return true;
        }
        return false;
    }

    private boolean isUserPostOwner(PostModel postModel, String userId, Map<String, Object> response) {
        if (postModel.getUserId().equals(userId)) {
            response.put("success", false);
            response.put("message", "To jest Twój post, więc nie możesz się do niego zapisać.");
            return true;
        }
        return false;
    }

    private boolean isUserAlreadyRegistered(String postId, String userId, Map<String, Object> response) {
        if (postsRegistrationService.isUserRegisteredForPost(postId, userId)) {
            response.put("success", false);
            response.put("message", "Już zostałeś zapisany do tego postu.");
            return true;
        }
        return false;
    }

    private boolean hasUserReachedPostLimit(String userId, Map<String, Object> response) {
        int registeredPostCount = postsRegistrationService.getRegisteredPostCountForUser(userId);
        if (registeredPostCount >= 3) {
            response.put("success", false);
            response.put("message", "Osiągnąłeś limit zapisanych postów (3).");
            return true;
        }
        return false;
    }

    private boolean isPostFull(PostModel postModel, Map<String, Object> response) {
        if (postModel.getSignedUpCount() >= postModel.getHowManyPeopleNeeded()) {
            response.put("success", false);
            response.put("message", "Osiągnięto limit zapisanych uczestników.");
            return true;
        }
        return false;
    }

    private Map<String, Object> buildRegistrationResponse(boolean success, Map<String, Object> response) {
        if (success) {
            response.put("success", true);
            response.put("message", "Zostałeś pomyślnie zapisany.");
        } else {
            response.put("success", false);
            response.put("message", "Błąd podczas zapisywania.");
        }
        return response;
    }
}