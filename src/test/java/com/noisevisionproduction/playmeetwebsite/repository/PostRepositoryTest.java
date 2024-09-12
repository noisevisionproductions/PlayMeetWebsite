package com.noisevisionproduction.playmeetwebsite.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostRepositoryTest {

    @Mock
    private Firestore firestore;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private ApiFuture<DocumentReference> future;

    @InjectMocks
    private PostRepository postRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePost() throws ExecutionException, InterruptedException {
        String postIdMock = "postIdMock";
        PostModel postModel = new PostModel();
        postModel.setPostId(postIdMock);

        when(firestore.collection("PostCreating")).thenReturn(collectionReference);
        when(collectionReference.add(postModel)).thenReturn(future);
        when(future.get()).thenReturn(documentReference);
        when(documentReference.getId()).thenReturn(postIdMock);

        String postId = postRepository.savePost(postModel);

        assertEquals(postIdMock, postId);
        assertEquals(postIdMock, postModel.getPostId());
        verify(documentReference).set(postModel);
    }
}