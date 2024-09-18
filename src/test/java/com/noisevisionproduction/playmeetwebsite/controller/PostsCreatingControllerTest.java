package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostCreatingService;
import com.noisevisionproduction.playmeetwebsite.service.PostsRegistrationService;
import com.noisevisionproduction.playmeetwebsite.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostsCreatingController.class)
class PostsCreatingControllerTest {

    @MockBean
    private PostsRegistrationService postsRegistrationService;

    @MockBean
    private PostCreatingService postCreatingService;

    @MockBean
    private CookieService cookieService;

    @MockBean
    private PostsService postsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PostsCreatingController(
                postCreatingService,
                cookieService
        )).build();
    }

    @Test
    void createPost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        PostRequest postRequest = new PostRequest();
        String userId = "userId";
        String postId = "postId";

        when(cookieService.getLoginStatusCookie(request)).thenReturn(userId);
        when(postCreatingService.createPost(any(PostRequest.class), eq(userId))).thenReturn(postId);

        mockMvc.perform(post("/api/posts/create"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/?postId=null"));
    }

    @Test
    void signUserToPost() throws Exception {
        String postId = "postId";
        String userId = "userId";

        PostModel postModel = new PostModel();
        postModel.setSignedUpCount(2);
        postModel.setHowManyPeopleNeeded(5);
        postModel.setUserId("userIdSecond");

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postsService.getPostByPostId(postId)).thenReturn(postModel);
        when(postsRegistrationService.isUserRegisteredForPost(postId, userId)).thenReturn(false);
        when(postsRegistrationService.getRegisteredPostCountForUser(userId)).thenReturn(1);
        when(postsRegistrationService.registerUserForPost(postId, userId)).thenReturn(true);

        performPostRequestAndExpectSuccess(postId, true, "Zostałeś pomyślnie zapisany.");
    }

    @Test
    void isUserNotLoggedIn() throws Exception {
        String postId = "postId";

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(null);

        performPostRequestAndExpectSuccess(postId, false, "Musisz być zalogowany, aby się zapisać");
    }

    @Test
    void isUserPostOwner() throws Exception {
        String postId = "postId";
        String userId = "userId";
        PostModel postModel = new PostModel();
        postModel.setUserId(userId);

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postsService.getPostByPostId(postId)).thenReturn(postModel);

        performPostRequestAndExpectSuccess(postId, false, "To jest Twój post, więc nie możesz się do niego zapisać.");
    }

    @Test
    void isUserAlreadyRegistered() throws Exception {
        String postId = "postId";
        String userId = "userId";

        PostModel postModel = new PostModel();
        postModel.setUserId("userIdSecond");

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postsService.getPostByPostId(postId)).thenReturn(postModel);
        when(postsRegistrationService.isUserRegisteredForPost(postId, userId)).thenReturn(true);

        performPostRequestAndExpectSuccess(postId, false, "Już zostałeś zapisany do tego postu.");
    }

    @Test
    void hasUserReachedPostLimit() throws Exception {
        String userId = "userId";
        String postId = "postId";

        PostModel postModel = new PostModel();
        postModel.setUserId("userIdSecond");

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postsService.getPostByPostId(postId)).thenReturn(postModel);
        when(postsRegistrationService.getRegisteredPostCountForUser(userId)).thenReturn(3);

        performPostRequestAndExpectSuccess(postId, false, "Osiągnąłeś limit zapisanych postów (3).");
    }

    @Test
    void isPostFull() throws Exception {
        String postId = "postId";
        String userId = "userId";

        PostModel postModel = new PostModel();
        postModel.setUserId("userIdNew");
        postModel.setSignedUpCount(1);
        postModel.setHowManyPeopleNeeded(1);

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postsService.getPostByPostId(postId)).thenReturn(postModel);

        performPostRequestAndExpectSuccess(postId, false, "Osiągnięto limit zapisanych uczestników.");
    }

    private void performPostRequestAndExpectSuccess(String postId, boolean expectedSuccess, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/posts/register-for-post")
                        .content("{\"postId\": \"" + postId + "\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(expectedSuccess))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}