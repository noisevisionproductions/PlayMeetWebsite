package com.noisevisionproduction.playmeetwebsite.service;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostsDetailsServiceTest {

    @Mock
    private PostsService postsServiceMock;

    @Mock
    private PostsRegistrationService postsRegistrationServiceMock;

    @Mock
    private UserService userServiceMock;

    @InjectMocks
    private PostsDetailsService postsDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPostsSuccess() throws ExecutionException, InterruptedException {
        List<PostModel> posts = new ArrayList<>();
        PostModel postModel = new PostModel();
        postModel.setPostId("post1");
        postModel.setUserId("user1");
        postModel.setHowManyPeopleNeeded(1);
        posts.add(postModel);

        UserModel userModel = new UserModel();
        userModel.setUserId("user1");
        userModel.setAvatar("userAvatar");

        Map<String, Object> registration = new HashMap<>();
        registration.put("userId", "user2");

        List<Map<String, Object>> listOfRegistrations = Collections.singletonList(registration);
        UserModel registeredUser = new UserModel();
        registeredUser.setUserId("user2");

        when(postsServiceMock.getPosts()).thenReturn(posts);
        when(userServiceMock.getUserById("user1")).thenReturn(CompletableFuture.completedFuture(userModel));
        when(postsRegistrationServiceMock.getRegistrationsForPost("post1")).thenReturn(listOfRegistrations);
        when(userServiceMock.getUserById("user2")).thenReturn(CompletableFuture.completedFuture(registeredUser));

        List<PostModel> result = postsDetailsService.getAllPosts();

        assertNotNull(result);
        assertFalse(posts.isEmpty());

        PostModel resultPost = posts.get(0);
        assertEquals("userAvatar", resultPost.getAvatar());
        assertEquals(1, resultPost.getRegistrations().size());
        assertEquals("user2", resultPost.getRegistrations().get(0).getUserId());

        verify(postsServiceMock, times(1)).getPosts();
        verify(userServiceMock, times(1)).getUserById("user1");
        verify(postsRegistrationServiceMock, times(1)).getRegistrationsForPost("post1");
        verify(userServiceMock, times(1)).getUserById("user2");
    }

    @Test
    public void testGetAllPostsEmpty() throws ExecutionException, InterruptedException {
        when(postsServiceMock.getPosts()).thenReturn(new ArrayList<>());

        List<PostModel> result = postsDetailsService.getAllPosts();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postsServiceMock, times(1)).getPosts();
        verifyNoMoreInteractions(userServiceMock, postsRegistrationServiceMock);
    }
}