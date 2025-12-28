package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.database.*;
import com.google.firebase.database.Transaction;
import com.noisevisionproduction.playmeetwebsite.model.RegistrationModel;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.cloud.Timestamp;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostsRegistrationService extends LogsPrint {

    private final PostsService postsService;
    private final Firestore firestore;
    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public PostsRegistrationService(PostsService postsService, Firestore firestore, FirebaseDatabase firebaseDatabase) {
        this.postsService = postsService;
        this.firestore = firestore;
        this.firebaseDatabase = firebaseDatabase;
    }

    public List<Map<String, Object>> getRegistrationsForPostByPostId(String postId) {
        try {
            ApiFuture<QuerySnapshot> apiFuture = firestore.collection("registrations")
                    .whereEqualTo("postId", postId)
                    .get();

            List<QueryDocumentSnapshot> documentSnapshots = apiFuture.get().getDocuments();
            return documentSnapshots.stream()
                    .map(QueryDocumentSnapshot::getData)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logError("Error fetching registrations for post ID: " + postId, e);
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getRegistrationsForPostByUserId(String userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("registrations")
                    .whereEqualTo("userId", userId)
                    .get();

            List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();
            return documentSnapshots.stream()
                    .map(QueryDocumentSnapshot::getData)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logError("Error fetching posts for user: " + userId, e);
            return Collections.emptyList();
        }
    }

    public boolean isUserRegisteredForPost(String postId, String userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("registrations")
                    .whereEqualTo("postId", postId)
                    .whereEqualTo("userId", userId)
                    .get();
            return !future.get().isEmpty();
        } catch (Exception e) {
            logError("Error checking if user is registered for post: " + postId, e);
            return false;
        }
    }

    public int getRegisteredPostCountForUser(String userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("registrations")
                    .whereEqualTo("userId", userId)
                    .get();
            return future.get().size();
        } catch (Exception e) {
            logError("Error fetching registration count for user: " + userId, e);
            return 0;
        }
    }

    public void updateUserJoinedPostsCount(String userId, boolean increment) {
        DatabaseReference userReference = firebaseDatabase.getReference("UserModel/" + userId);
        userReference.child("joinedPostsCount").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                Integer userCount = currentData.getValue(Integer.class);

                if (userCount == null) {
                    userCount = 0;
                }

                if (increment) {
                    currentData.setValue(userCount + 1);
                } else if (userCount > 0) {
                    currentData.setValue(userCount - 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    logError("Error updating post count for user: " + userId, error.toException());
                } else if (committed) {
                    logDebug("Successfully updated post count for user: " + userId);
                }
            }
        });
    }

    public boolean registerUserForPost(String postId, String userId) {
        try {
            RegistrationModel registrationModel = new RegistrationModel();
            registrationModel.setPostId(postId);
            registrationModel.setUserId(userId);
            registrationModel.setRegistrationDate(Timestamp.now());
            firestore.collection("registrations").add(registrationModel);

            updateUserJoinedPostsCount(userId, true);
            postsService.incrementSignedUpCount(postId, true);

            return true;
        } catch (Exception e) {
            logError("Error registering user for post: " + postId, e);
            return false;
        }
    }

    public boolean unregisterUserFromThePost(String postId, String userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("registrations")
                    .whereEqualTo("postId", postId)
                    .whereEqualTo("userId", userId)
                    .get();

            List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();

            if (!documentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    firestore.collection("registrations").document(document.getId()).delete();
                }

                updateUserJoinedPostsCount(userId, false);
                postsService.incrementSignedUpCount(postId, false);
            }
            return true;
        } catch (Exception e) {
            logError("Error unregistering user from post: " + postId, e);
            return false;
        }
    }
}
