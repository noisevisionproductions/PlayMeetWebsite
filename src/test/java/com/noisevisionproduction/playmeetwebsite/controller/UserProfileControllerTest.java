package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostsDetailsService;
import com.noisevisionproduction.playmeetwebsite.service.UserService;
import com.noisevisionproduction.playmeetwebsite.service.dataEncryption.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserProfileControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private CookieService cookieService;

    @Mock
    private PostsDetailsService postsDetailsService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Model model;

    @InjectMocks
    private UserProfileController userProfileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProfile() throws Exception {
        String userId = "userId";
        String loggedInUserId = "userId";
        UserModel userModel = new UserModel();
        userModel.setName("EncryptedName");
        userModel.setAge("EncryptedAge");
        userModel.setLocation("EncryptedLocation");
        userModel.setGender("EncryptedGender");
        userModel.setAboutMe("EncryptedAboutMe");

        List<PostModel> posts = Arrays.asList(new PostModel("post1"), new PostModel("post2"));

        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        when(postsDetailsService.getUserPosts(userId)).thenReturn(posts);
        when(cookieService.getLoginStatusCookie(httpServletRequest)).thenReturn(loggedInUserId);

        when(encryptionService.decrypt("EncryptedName")).thenReturn("DecryptedName");
        when(encryptionService.decrypt("EncryptedAge")).thenReturn("DecryptedAge");
        when(encryptionService.decrypt("EncryptedLocation")).thenReturn("DecryptedLocation");
        when(encryptionService.decrypt("EncryptedGender")).thenReturn("DecryptedGender");
        when(encryptionService.decrypt("EncryptedAboutMe")).thenReturn("DecryptedAboutMe");

        String viewName = userProfileController.getProfile(userId, model, httpServletRequest);

        verify(encryptionService).decrypt("EncryptedName");
        verify(encryptionService).decrypt("EncryptedAge");
        verify(encryptionService).decrypt("EncryptedLocation");
        verify(encryptionService).decrypt("EncryptedGender");
        verify(encryptionService).decrypt("EncryptedAboutMe");

        verify(model).addAttribute(eq("user"), any(UserModel.class));
        verify(model).addAttribute(eq("posts"), eq(posts));
        verify(model).addAttribute(eq("isOwnProfile"), eq(true));

        assertEquals("user_account", viewName);
    }

    @Test
    public void testGetProfileWithDifferentUser() throws Exception {
        String userId = "userId";
        String loggedInUserId = "loggedInUserId";
        UserModel userModel = new UserModel();
        userModel.setName("EncryptedName");

        List<PostModel> posts = List.of(new PostModel("post1"));

        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        when(postsDetailsService.getUserPosts(userId)).thenReturn(posts);
        when(cookieService.getLoginStatusCookie(httpServletRequest)).thenReturn(loggedInUserId);
        when(encryptionService.decrypt("EncryptedName")).thenReturn("DecryptedName");

        String viewName = userProfileController.getProfile(userId, model, httpServletRequest);

        verify(model).addAttribute(eq("isOwnProfile"), eq(false));
        assertEquals("user_account", viewName);
    }

    @Test
    public void testGetProfileDecryptionError() throws Exception {
        String userId = "userId";
        UserModel userModel = new UserModel();
        userModel.setName("EncryptedName");

        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        when(encryptionService.decrypt("EncryptedName")).thenThrow(new RuntimeException("Decryption error"));

        userProfileController.getProfile(userId, model, httpServletRequest);

        verify(encryptionService).decrypt("EncryptedName");
        verify(model).addAttribute(eq("user"), any(UserModel.class));
    }
}