package com.noisevisionproduction.playmeetwebsite.controller;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.service.PostsDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostsControllerTest {

    @Mock
    private PostsDetailsService postsDetailsServiceMock;

    @Mock
    private Model modelMock;

    @InjectMocks
    private PostsController postsController;

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

        when(postsDetailsServiceMock.getAllPosts()).thenReturn(posts);

        String viewName = postsController.showPostPage(modelMock);

        assertEquals("posts", viewName);
        verify(modelMock, times(1)).addAttribute("posts", posts);
    }

    @Test
    public void testShowPostPageError() throws ExecutionException, InterruptedException {
        when(postsDetailsServiceMock.getAllPosts()).thenThrow(new ExecutionException(new RuntimeException("Error")));

        String viewName = postsController.showPostPage(modelMock);

        assertEquals("error", viewName);
        verify(modelMock, times(1)).addAttribute("error", "Error fetching posts");
    }
}