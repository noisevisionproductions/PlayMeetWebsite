package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.*;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.*;
import com.noisevisionproduction.playmeetwebsite.model.RegistrationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private PostsService postsService;

    @Mock
    private Query queryMock;

    @InjectMocks
    @Spy
    private PostsRegistrationService postsRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(firestoreMock.collection("registrations")).thenReturn(collectionReferenceMock);
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

        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertFalse(registrations.isEmpty());
        assertEquals("testUser", registrations.get(0).get("userId"));

        // Throw error
        when(futureMock.get()).thenThrow(new RuntimeException("Error fetching registrations for post ID"));

        List<Map<String, Object>> errorRegistrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(errorRegistrations);
        assertTrue(errorRegistrations.isEmpty());
    }

    @Test
    public void testGetRegistrationsForPostEmpty() throws ExecutionException, InterruptedException {
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.getDocuments()).thenReturn(new ArrayList<>());
        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }

    @Test
    public void testGetRegistrationsForPostThrowsException() throws ExecutionException, InterruptedException {
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(collectionReferenceMock.whereEqualTo("postId", "12345")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost("12345");

        assertNotNull(registrations);
        assertTrue(registrations.isEmpty());
    }

    @Test
    void isUserRegisteredForPost() throws ExecutionException, InterruptedException {
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.isEmpty()).thenReturn(false);
        when(collectionReferenceMock.whereEqualTo("postId", "postId")).thenReturn(queryMock);
        when(queryMock.whereEqualTo("userId", "userId")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);

        boolean result = postsRegistrationService.isUserRegisteredForPost("postId", "userId");
        assertTrue(result);

        when(futureMock.get()).thenThrow(new RuntimeException("Error checking if user is registered for post"));
        boolean errorResult = postsRegistrationService.isUserRegisteredForPost("postId", "userId");
        assertFalse(errorResult);
    }

    @Test
    void getRegisteredPostCountForUser() throws ExecutionException, InterruptedException {
        when(collectionReferenceMock.whereEqualTo("userId", "userId")).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(futureMock);
        when(futureMock.get()).thenReturn(querySnapshotMock);
        when(querySnapshotMock.size()).thenReturn(5);

        int result = postsRegistrationService.getRegisteredPostCountForUser("userId");
        assertEquals(5, result);

        when(queryMock.get()).thenThrow(new RuntimeException("Error fetching registration count for user"));
        int errorResult = postsRegistrationService.getRegisteredPostCountForUser("userId");
        assertEquals(0, errorResult);
    }

    @Test
    void incrementUserPostCount() {
        String userId = "userId";
        MutableData mutableData = mock(MutableData.class);
        DatabaseReference databaseReference = mock(DatabaseReference.class);
        DatabaseReference childReference = mock(DatabaseReference.class);

        when(firebaseDatabase.getReference(anyString())).thenReturn(databaseReference);
        when(databaseReference.child(anyString())).thenReturn(childReference);

        // When post count is 1, then it should increment to 2
        when(mutableData.getValue(Integer.class)).thenReturn(1);

        doAnswer(invocation -> {
            Transaction.Handler handler = invocation.getArgument(0);
            handler.doTransaction(mutableData);
            handler.onComplete(null, true, null);
            return null;
        }).when(childReference).runTransaction(any(Transaction.Handler.class));

        postsRegistrationService.incrementUserPostCount(userId);

        verify(mutableData).setValue(2);
        verify(childReference).runTransaction(any(Transaction.Handler.class));

        // Case when post count is null
        when(mutableData.getValue(Integer.class)).thenReturn(null);
        postsRegistrationService.incrementUserPostCount(userId);
        verify(mutableData).setValue(1);

        // Failure simulation
        doAnswer(invocation -> {
            Transaction.Handler handler = invocation.getArgument(0);
            handler.onComplete(DatabaseError.fromCode(DatabaseError.DISCONNECTED), false, null);
            return null;
        }).when(childReference).runTransaction(any(Transaction.Handler.class));

        postsRegistrationService.incrementUserPostCount(userId);
    }

    @Test
    void registerUserForPost() throws ExecutionException, InterruptedException {
        String postId = "postId";
        String userId = "userId";
        ApiFuture<DocumentReference> apiFuture = mock(ApiFuture.class);
        DocumentReference documentReference = mock(DocumentReference.class);

        when(collectionReferenceMock.add(any(RegistrationModel.class))).thenReturn(apiFuture);
        when(apiFuture.get()).thenReturn(documentReference);

        doNothing().when(postsService).incrementSignedUpCount(postId, true);
        doNothing().when(postsRegistrationService).incrementUserPostCount(userId);

        boolean result = postsRegistrationService.registerUserForPost(postId, userId);

        assertTrue(result);
        verify(collectionReferenceMock).add(any(RegistrationModel.class));
        verify(postsService).incrementSignedUpCount(postId, true);
        verify(postsRegistrationService).incrementUserPostCount(userId);

        // Throwing error
        doThrow(new RuntimeException("Error registering user for post")).when(collectionReferenceMock).add(any(RegistrationModel.class));

        boolean errorResult = postsRegistrationService.registerUserForPost(postId, userId);

        assertFalse(errorResult);
    }
}