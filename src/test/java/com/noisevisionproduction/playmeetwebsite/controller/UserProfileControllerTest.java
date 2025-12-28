package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Mock
    private FileStorageService fileStorageService;

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

        List<PostModel> userPosts = Arrays.asList(new PostModel("post1"), new PostModel("post2"));
        List<PostModel> userRegisteredPosts = Arrays.asList(new PostModel("post3"), new PostModel("post4"));

        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        when(postsDetailsService.getUserPosts(userId)).thenReturn(userPosts);
        when(postsDetailsService.getPostsWhereUserRegistered(userId)).thenReturn(userRegisteredPosts);
        when(cookieService.getLoginStatusCookie(httpServletRequest)).thenReturn(loggedInUserId);

        doNothing().when(encryptionService).decryptUserData(userModel);

        String viewName = userProfileController.getProfile(userId, model, httpServletRequest);

        verify(encryptionService).decryptUserData(userModel);
        verify(model).addAttribute(eq("user"), any(UserModel.class));
        verify(model).addAttribute(eq("loggedInUserId"), eq(loggedInUserId));
        verify(model).addAttribute(eq("userPosts"), eq(userPosts));
        verify(model).addAttribute(eq("userRegisteredPosts"), eq(userRegisteredPosts));
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

        verify(encryptionService).decryptUserData(userModel);
        verify(model).addAttribute(eq("user"), any(UserModel.class));
    }

    @Test
    void editProfile_RedirectWhenNotProfileOwner() {
        String userId = "userId";
        String loggedInUserId = "loggedInUserId";

        when(cookieService.getLoginStatusCookie(httpServletRequest)).thenReturn(loggedInUserId);

        String viewName = userProfileController.editProfile(userId, model, httpServletRequest);

        assertEquals("redirect:/user_account/" + userId, viewName);

        verify(userService, never()).getUserById(any());
        verify(encryptionService, never()).decryptUserData(any());
    }

    @Test
    void editProfile_RedirectToEditProfile() {
        String userId = "userId";
        String loggedInUserId = "userId";

        UserModel userModel = new UserModel();
        userModel.setName("Name");

        when(cookieService.getLoginStatusCookie(httpServletRequest)).thenReturn(loggedInUserId);
        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        doNothing().when(encryptionService).decryptUserData(any(UserModel.class));

        String viewName = userProfileController.editProfile(userId, model, httpServletRequest);

        assertEquals("user_account_edit", viewName);

        verify(userService).getUserById(userId);
        verify(encryptionService).decryptUserData(any(UserModel.class));
        verify(model).addAttribute(eq("user"), eq(userModel));
        verify(model).addAttribute(eq("isOwnProfile"), eq(true));
    }

    @Test
    void updateProfile_SuccessWithAvatar() throws IOException {
        String userId = "userId";
        UserModel userModel = new UserModel();
        userModel.setName("User");

        MultipartFile avatarFile = mock(MultipartFile.class);
        when(avatarFile.isEmpty()).thenReturn(false);
        when(fileStorageService.uploadAvatarToFirebase(avatarFile, userId)).thenReturn("avatarUrl");

        doNothing().when(encryptionService).decryptUserData(userModel);
        doNothing().when(userService).updateUser(userId, userModel);

        String viewName = userProfileController.updateProfile(userId, userModel, avatarFile, model);

        assertEquals("user_account_edit_success", viewName);
        verify(fileStorageService).uploadAvatarToFirebase(avatarFile, userId);
        verify(encryptionService).encryptUserData(userModel);
        verify(userService).updateUser(userId, userModel);
        verify(model).addAttribute("updateSuccess", true);
    }

    @Test
    void updateProfile_SuccessWithoutAvatar() throws IOException {
        String userId = "userId";
        UserModel userModel = new UserModel();
        userModel.setName("User");

        MultipartFile avatarFile = mock(MultipartFile.class);
        when(avatarFile.isEmpty()).thenReturn(true);

        doNothing().when(encryptionService).decryptUserData(userModel);
        doNothing().when(userService).updateUser(userId, userModel);

        String viewName = userProfileController.updateProfile(userId, userModel, avatarFile, model);

        assertEquals("user_account_edit_success", viewName);
        verify(fileStorageService, never()).uploadAvatarToFirebase(any(), any());
        verify(encryptionService).encryptUserData(userModel);
        verify(userService).updateUser(userId, userModel);
        verify(model).addAttribute("updateSuccess", true);
    }

    @Test
    void updateProfile_AvatarUploadFailure() throws IOException {
        String userId = "userId";
        UserModel userModel = new UserModel();
        userModel.setName("User");

        MultipartFile avatarFile = mock(MultipartFile.class);
        when(avatarFile.isEmpty()).thenReturn(false);
        when(fileStorageService.uploadAvatarToFirebase(avatarFile, userId)).thenThrow(new RuntimeException("Wystąpił błąd podczas przesyłania avatara."));

        String viewName = userProfileController.updateProfile(userId, userModel, avatarFile, model);

        assertEquals("user_account_edit_success", viewName);
        verify(fileStorageService).uploadAvatarToFirebase(avatarFile, userId);
        verify(model).addAttribute(eq("updateError"), contains("Wystąpił błąd podczas przesyłania avatara."));
    }

    @Test
    void deleteAvatar_Success() throws IOException {
        String userId = "userId";
        UserModel userModel = new UserModel();
        userModel.setAvatar("avatarUrl");

        doNothing().when(fileStorageService).deleteAvatarFromFirebase(userId);
        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(userModel));
        doNothing().when(userService).updateUserAvatar(userId, null);

        String viewName = userProfileController.deleteAvatar(userId, model);

        assertEquals("user_account_edit", viewName);

        verify(fileStorageService).deleteAvatarFromFirebase(userId);
        verify(userService).getUserById(userId);
        verify(userService).updateUserAvatar(userId, null);
        verify(model).addAttribute("deleteSuccess", true);
    }

    @Test
    void deleteAvatar_Failure() throws IOException {
        String userId = "userId";

        doThrow(new RuntimeException("Deletion error")).when(fileStorageService).deleteAvatarFromFirebase(userId);

        String viewName = userProfileController.deleteAvatar(userId, model);

        assertEquals("user_account_edit", viewName);

        verify(fileStorageService).deleteAvatarFromFirebase(userId);
        verify(model).addAttribute(eq("deleteError"), contains("Wystąpił błąd podczas usuwania avatara: Deletion error"));
    }
}