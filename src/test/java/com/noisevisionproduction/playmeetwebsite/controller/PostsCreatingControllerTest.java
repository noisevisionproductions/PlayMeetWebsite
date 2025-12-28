package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.service.CookieService;
import com.noisevisionproduction.playmeetwebsite.service.PostCreatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsCreatingController.class)
class PostsCreatingControllerTest {

    @MockBean
    private PostCreatingService postCreatingService;

    @MockBean
    private CookieService cookieService;

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
        String userId = "userId";
        String postId = "postId";

        when(cookieService.getLoginStatusCookie(any(HttpServletRequest.class))).thenReturn(userId);
        when(postCreatingService.createPost(any(PostRequest.class), eq(userId))).thenReturn(postId);

        mockMvc.perform(post("/api/posts/create")
                        .param("createdByUser", "true")
                        .param("isActivityFull", "false")
                        .param("userId", userId)
                        .param("postId", postId)
                        .param("sportName", "Football")
                        .param("skillLevel", "1")
                        .param("howManyPeopleNeeded", "1"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/?postId=" + postId));
    }
}