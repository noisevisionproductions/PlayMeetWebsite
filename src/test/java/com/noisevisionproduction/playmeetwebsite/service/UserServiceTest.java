package com.noisevisionproduction.playmeetwebsite.service;

import com.google.firebase.database.*;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private DatabaseReference databaseReference;

    @Mock
    private DataSnapshot dataSnapshot;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByIdSuccess() throws ExecutionException, InterruptedException {
        String userId = "testId";
        UserModel userModel = new UserModel();
        userModel.setUserId(userId);

        when(firebaseDatabase.getReference("UserModel/" + userId)).thenReturn(databaseReference);
        when(dataSnapshot.getValue(UserModel.class)).thenReturn(userModel);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(dataSnapshot);
            return null;
        }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        CompletableFuture<UserModel> resultFuture = userService.getUserById(userId);
        UserModel result = resultFuture.get();

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testGetUserByIdFailure() {
        String userId = "testId";
        DatabaseError databaseError = Mockito.mock(DatabaseError.class);
        DatabaseException exception = new DatabaseException("Firebase error");

        when(firebaseDatabase.getReference("UserModel/" + userId)).thenReturn(databaseReference);
        when(databaseError.toException()).thenReturn(exception);

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onCancelled(DatabaseError.fromException(exception));
            return null;
        }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        CompletableFuture<UserModel> resultFuture = userService.getUserById(userId);

        assertTrue(resultFuture.isCompletedExceptionally());

        ExecutionException executionException = assertThrows(ExecutionException.class, resultFuture::get);

        assertTrue(executionException.getCause().getMessage().contains("Firebase error"));
    }
}