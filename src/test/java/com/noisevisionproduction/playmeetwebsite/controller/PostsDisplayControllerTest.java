package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.service.PostsDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostsDisplayControllerTest {

    @Mock
    private PostsDetailsService postsDetailsServiceMock;

    @Mock
    private Model modelMock;

    @InjectMocks
    private PostsDisplayController postsDisplayController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowPostPageSuccess() throws Exception {
        List<PostModel> posts = new ArrayList<>();
        PostModel postModel = new PostModel();
        postModel.setPostId("12345");
        posts.add(postModel);

        HttpServletRequest request = new MockHttpServletRequest();

        when(postsDetailsServiceMock.getAllPosts()).thenReturn(posts);

        String viewName = postsDisplayController.showPostPage(modelMock, request);

        assertEquals("posts", viewName);
        verify(modelMock, times(1)).addAttribute("posts", posts);
    }

    @Test
    public void testShowPostPageError() throws ExecutionException, InterruptedException {
        when(postsDetailsServiceMock.getAllPosts()).thenThrow(new ExecutionException(new RuntimeException("Error")));
        HttpServletRequest request = new MockHttpServletRequest();

        String viewName = postsDisplayController.showPostPage(modelMock, request);

        assertEquals("error", viewName);
        verify(modelMock, times(1)).addAttribute("error", "Error fetching posts");
    }
}