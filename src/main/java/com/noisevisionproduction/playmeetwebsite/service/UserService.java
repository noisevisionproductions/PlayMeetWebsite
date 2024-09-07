package com.noisevisionproduction.playmeetwebsite.service;

import com.google.firebase.database.*;
import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService extends LogsPrint {

    private final FirebaseDatabase firebaseDatabase;

    public UserService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public CompletableFuture<UserModel> getUserById(String userId) {
        DatabaseReference userReference = firebaseDatabase.getReference("UserModel/" + userId);
        CompletableFuture<UserModel> completableFuture = new CompletableFuture<>();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                completableFuture.complete(userModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                completableFuture.completeExceptionally(databaseError.toException());
                logError("Error realtime database ", databaseError.toException());
            }
        });
        return completableFuture;
    }
}
