package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PostsRegistrationServiceTest {

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

    @Mock
    private Query queryMock;

    @InjectMocks
    private PostsRegistrationService postsRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRegistrationsForPost() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documentSnapshots = new ArrayList<>();
        Map<String, Object> registrationData = new HashMap<>();
        registrationData.put("userId", "testUser");
        documentSnapshots.add(documentSnapshotMock);

        when(documentSnapshotMock.getData()).thenReturn(registrationData);
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(documentSnapshots);

        when(firestoreMock.collection("registrations")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertFalse(registrations.isEmpty());
        assertEquals("testUser", registrations.get(0).get("userId"));
    }

    @Test
    public void testGetRegistrationsForPostEmpty() throws ExecutionException, InterruptedException {
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(new ArrayList<>());
        when(firestoreMock.collection("registrations")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }

    @Test
    public void testGetRegistrationsForPostThrowsException() {
        when(firestoreMock.collection("registrations")).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }
}