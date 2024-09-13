package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostsServiceTest {

    @Mock
    private Firestore firestoreMock;

    @Mock
    private ApiFuture<QuerySnapshot> futureMock;

    @Mock
    private ApiFuture<DocumentSnapshot> documentSnapshotApiFuture;

    @Mock
    private QuerySnapshot querySnapshotMock;

    @Mock
    private QueryDocumentSnapshot documentSnapshotMock;

    @Mock
    private CollectionReference collectionReferenceMock;

    @InjectMocks
    private PostsService postsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() throws InterruptedException, ExecutionException {
        when(firestoreMock.collection("PostCreating")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.get()).thenReturn(futureMock);
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(List.of(documentSnapshotMock));
        when(documentSnapshotMock.toObject(PostModel.class)).thenReturn(new PostModel());

        List<PostModel> posts = postsService.getPosts();

        assertNotNull(posts);
        assertEquals(1, posts.size());
        verify(firestoreMock.collection("PostCreating")).get();

        // Throw error
        when(futureMock.get()).thenThrow(new RuntimeException("Error fetching posts from Firestore"));
        List<PostModel> errorPosts = postsService.getPosts();
        assertNotNull(errorPosts);
        assertTrue(errorPosts.isEmpty());
    }

    @Test
    void getPostsByUserId() throws ExecutionException, InterruptedException {
        String userId = "userId";

        Query query = mock(Query.class);

        when(firestoreMock.collection("PostCreating")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.whereEqualTo("userId", userId)).thenReturn(query);
        when(query.get()).thenReturn(futureMock);
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(List.of(documentSnapshotMock));
        when(documentSnapshotMock.toObject(PostModel.class)).thenReturn(new PostModel());

        List<PostModel> postModels = postsService.getPostsByUserId(userId);

        assertNotNull(postModels);
        assertEquals(1, postModels.size());
        verify(firestoreMock.collection("PostCreating").whereEqualTo("userId", userId)).get();

        // Throw error
        when(futureMock.get()).thenThrow(new RuntimeException("Error fetching user's posts"));
        List<PostModel> errorPost = postsService.getPostsByUserId(userId);
        assertNotNull(errorPost);
        assertTrue(errorPost.isEmpty());
    }

    @Test
    void getPostById() throws ExecutionException, InterruptedException {
        String postId = "postId";
        DocumentReference documentReferenceMock = mock(DocumentReference.class);

        when(firestoreMock.collection("PostCreating")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.document(postId)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.get())
                .thenReturn(documentSnapshotApiFuture)
                .thenThrow(new RuntimeException("Error fetching post by ID"));
        when(documentSnapshotApiFuture.get()).thenReturn(documentSnapshotMock);
        when(documentSnapshotMock.exists()).thenReturn(true);
        when(documentSnapshotMock.toObject(PostModel.class)).thenReturn(new PostModel());

        PostModel postModel = postsService.getPostById(postId);

        assertNotNull(postModel, "PostModel should not be null for a successful retrieval");
        verify(firestoreMock).collection("PostCreating");
        verify(collectionReferenceMock).document(postId);
        verify(documentReferenceMock).get();
        verify(documentSnapshotApiFuture).get();
        verify(documentSnapshotMock).exists();
        verify(documentSnapshotMock).toObject(PostModel.class);

        PostModel errorPostModel = postsService.getPostById(postId);

        assertNull(errorPostModel, "PostModel should be null when an exception occurs");
        verify(firestoreMock, times(2)).collection("PostCreating");
        verify(collectionReferenceMock, times(2)).document(postId);
        verify(documentReferenceMock, times(2)).get();
    }
}