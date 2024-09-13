package com.noisevisionproduction.playmeetwebsite.service;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostCreatingServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostCreatingService postCreatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost() throws ExecutionException, InterruptedException {
        PostRequest postRequest = new PostRequest();
        postRequest.setSportName("Football");
        postRequest.setCityName("Warsaw");
        postRequest.setNumberOfPeople(5);
        postRequest.setDateTime("2024-09-12");
        postRequest.setHourTime("15:00");
        postRequest.setSkillLevel(2);
        postRequest.setNoDateTime(false);
        postRequest.setNoHourTime(false);
        postRequest.setAdditionalInfo("Bring your own ball");

        String userId = "userId";

        when(postRepository.savePost(any(PostModel.class))).thenReturn("postId");

        String result = postCreatingService.createPost(postRequest, userId);

        assertEquals("postId", result);

        ArgumentCaptor<PostModel> postModelArgumentCaptor = ArgumentCaptor.forClass(PostModel.class);
        verify(postRepository).savePost(postModelArgumentCaptor.capture());

        PostModel capturedPostModel = postModelArgumentCaptor.getValue();
        assertEquals("Football", capturedPostModel.getSportType());
        assertEquals("Warsaw", capturedPostModel.getCityName());
        assertEquals(5, capturedPostModel.getHowManyPeopleNeeded());
        assertEquals("2024-09-12", capturedPostModel.getDateTime());
        assertEquals("15:00", capturedPostModel.getHourTime());
        assertEquals(2, capturedPostModel.getSkillLevel());
        assertEquals("userId", capturedPostModel.getUserId());
        assertEquals("Bring your own ball", capturedPostModel.getAdditionalInfo());
    }

    @Test
    void createPostError() throws ExecutionException, InterruptedException {
        PostRequest postRequest = new PostRequest();
        postRequest.setSportName("Basketball");
        postRequest.setCityName("Katowice");
        postRequest.setNumberOfPeople(3);
        postRequest.setAdditionalInfo("");

        String userId = "userId";

        when(postRepository.savePost(any(PostModel.class))).thenThrow(new RuntimeException("Database error"));

        String result = postCreatingService.createPost(postRequest, userId);

        assertNull(result);

        verify(postRepository).savePost(any(PostModel.class));
    }
}