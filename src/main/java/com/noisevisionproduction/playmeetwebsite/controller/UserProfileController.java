package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.service.*;
import com.noisevisionproduction.playmeetwebsite.service.dataEncryption.EncryptionService;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/user_account")
public class UserProfileController extends LogsPrint {

    private final UserService userService;
    private final EncryptionService encryptionService;
    private final CookieService cookieService;
    private final PostsDetailsService postsDetailsService;
    private final FileStorageService fileStorageService;
    private final PostsRegistrationService postsRegistrationService;
    private final PostsService postsService;

    @Autowired
    public UserProfileController(UserService userService, EncryptionService encryptionService, CookieService cookieService, PostsDetailsService postsDetailsService, FileStorageService fileStorageService, PostsRegistrationService postsRegistrationService, PostsService postsService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.cookieService = cookieService;
        this.postsDetailsService = postsDetailsService;
        this.fileStorageService = fileStorageService;
        this.postsRegistrationService = postsRegistrationService;
        this.postsService = postsService;
    }

    @GetMapping("/{userId}")
    public String getProfile(@PathVariable("userId") String userId, Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        List<PostModel> userPosts = postsDetailsService.getUserPosts(userId);
        List<PostModel> userRegisteredPosts = postsDetailsService.getPostsWhereUserRegistered(userId);

        model.addAttribute("loggedInUserId", getLoggedInUser(request));
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userRegisteredPosts", userRegisteredPosts);

        populateUserProfile(userId, model, request);

        return "user_account";
    }

    @GetMapping("/{userId}/edit")
    public String editProfile(@PathVariable String userId, Model model, HttpServletRequest request) {
        if (!userId.equals(getLoggedInUser(request))) {
            return "redirect:/user_account/" + userId;
        }

        populateUserProfile(userId, model, request);

        return "user_account_edit";
    }

    @PostMapping("/{userId}/edit")
    public String updateProfile(@PathVariable String userId, @ModelAttribute("user") UserModel userModel, @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile, Model model) {
        try {
            if (!avatarFile.isEmpty()) {
                String avatarUrl = fileStorageService.uploadAvatarToFirebase(avatarFile, userId);
                userModel.setAvatar(avatarUrl);
            }
            encryptionService.encryptUserData(userModel);
            userService.updateUser(userId, userModel);
            model.addAttribute("updateSuccess", true);
        } catch (Exception e) {
            model.addAttribute("updateError", "Wystąpił błąd podczas przesyłania avatara." + e);
            logError("Avatar update error: ", e);
        }

        return "user_account_edit_success";
    }

    @PostMapping("/{userId}/delete-avatar")
    public String deleteAvatar(@PathVariable String userId, Model model) {
        try {
            fileStorageService.deleteAvatarFromFirebase(userId);

            UserModel userModel = userService.getUserById(userId).join();
            userModel.setAvatar(null);
            userService.updateUserAvatar(userId, null);

            model.addAttribute("deleteSuccess", true);
        } catch (Exception e) {
            model.addAttribute("deleteError", "Wystąpił błąd podczas usuwania avatara: " + e.getMessage());
            logError("Avatar delete error: ", e);
        }
        return "user_account_edit";
    }

    private void populateUserProfile(String userId, Model model, HttpServletRequest request) {
        CompletableFuture<UserModel> userFuture = userService.getUserById(userId);
        UserModel userModel = userFuture.join();
        encryptionService.decryptUserData(userModel);
        boolean isOwnProfile = userId.equals(getLoggedInUser(request));

        model.addAttribute("user", userModel);
        model.addAttribute("isOwnProfile", isOwnProfile);
    }

    private String getLoggedInUser(HttpServletRequest request) {
        return cookieService.getLoginStatusCookie(request);
    }
}
