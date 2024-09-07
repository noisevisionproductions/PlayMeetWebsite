package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostsDetailsService;
import com.noisevisionproduction.playmeetwebsite.service.UserService;
import com.noisevisionproduction.playmeetwebsite.service.dataEncryption.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    public UserProfileController(UserService userService, EncryptionService encryptionService, CookieService cookieService, PostsDetailsService postsDetailsService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.cookieService = cookieService;
        this.postsDetailsService = postsDetailsService;
    }

    @GetMapping
    public String getProfile(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        String userId = (cookieService.getLoginStatusCookie(request));

        if (userId != null) {
            CompletableFuture<UserModel> userFuture = userService.getUserById(userId);
            List<PostModel> userPosts = postsDetailsService.getUserPosts(userId);

            UserModel userModel = userFuture.join();

            try {
                userModel.setName(encryptionService.decrypt(userModel.getName()));
                userModel.setAge(encryptionService.decrypt(userModel.getAge()));
                userModel.setLocation(encryptionService.decrypt(userModel.getLocation()));
                userModel.setGender(encryptionService.decrypt(userModel.getGender()));
                userModel.setAboutMe(encryptionService.decrypt(userModel.getAboutMe()));
            } catch (Exception e) {
                logError("Decryption error: ", e);
            }
            model.addAttribute("user", userModel);
            model.addAttribute("posts", userPosts);
        } else {
            return "redirect:/login";
        }
        return "user_account";
    }
}
