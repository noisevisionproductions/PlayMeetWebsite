package com.noisevisionproduction.playmeetwebsite.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.noisevisionproduction.playmeetwebsite.PostsManagement.PostModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostRepositoryTest {

    @Mock
    private Firestore firestoreMock;

    @Mock
    private ApiFuture<QuerySnapshot> futureMock;

    @Mock
    private QuerySnapshot querySnapshotMock;

    @Mock
    private QueryDocumentSnapshot documentSnapshotMock;

    @Mock
    private CollectionReference collectionReferenceMock;

    @InjectMocks
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() throws InterruptedException, ExecutionException {
        List<QueryDocumentSnapshot> documentSnapshots = new ArrayList<>();

        documentSnapshots.add(documentSnapshotMock);

        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(documentSnapshots);
        when(documentSnapshotMock.toObject(PostModel.class)).thenReturn(new PostModel());
        when(firestoreMock.collection("PostCreating")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.get()).thenReturn(futureMock);

        List<PostModel> posts = postRepository.getPosts();

        assertNotNull(posts);
    }
}