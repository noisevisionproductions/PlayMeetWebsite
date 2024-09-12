package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class is responsible for access to posts information from Firestore.
 *
 * @Service annotation indicates that this class is service component in Spring,
 * which will be automatically
 * detected during path scanning and creates from them Beans
 * (singletons), which can be injected as dependencies
 * in other parts of the Spring application.
 */
@Service
public class PostsService extends LogsPrint {

    private final Firestore firestore;

    @Autowired
    public PostsService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Method is responsible for getting posts list from Firestore collection
     * "PostCreating".
     * It uses asynchronous API Firestore for document inquiries, so operations
     * won't be blocked
     * and potentially negatively affects app performance.
     */
    public List<PostModel> getPosts() {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("PostCreating").get();

            List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();
            return documentSnapshots.stream()
                    .map(document -> document.toObject(PostModel.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logError("Error fetching posts from Firestore ", e);
            return Collections.emptyList();
        }
    }

    public List<PostModel> getPostsByUserId(String userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("PostCreating")
                    .whereEqualTo("userId", userId)
                    .get();

            List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();
            return documentSnapshots.stream()
                    .map(document -> document.toObject(PostModel.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logError("Error fetching user's posts ", e);
            return Collections.emptyList();
        }
    }

    public PostModel getPostById(String postId) {
        try {
            DocumentReference documentReference = firestore.collection("PostCreating").document(postId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                return documentSnapshot.toObject(PostModel.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            logError("Error fetching post by ID: " + postId, e);
            return null;
        }
    }

    public void incrementSignedUpCount(String postId, boolean increment) {
        DocumentReference postReference = firestore.collection("PostCreating").document(postId);

        ApiFuture<Void> transactionFuture = firestore.runTransaction(transaction -> {
            DocumentSnapshot postSnapshot = transaction.get(postReference).get();

            int currentSignedUpCount = Objects.requireNonNull(postSnapshot.getLong("signedUpCount")).intValue();
            int howManyPeopleNeeded = Objects.requireNonNull(postSnapshot.getLong("howManyPeopleNeeded")).intValue();

            int newSignedUpCount;
            if (increment) {
                newSignedUpCount = currentSignedUpCount + 1;
            } else {
                newSignedUpCount = Math.max(0, currentSignedUpCount - 1);
            }

            boolean isActivityFull = newSignedUpCount >= howManyPeopleNeeded;
            String newPeopleStatus = newSignedUpCount + "/" + howManyPeopleNeeded;

            transaction.update(postReference, "signedUpCount", newSignedUpCount);
            transaction.update(postReference, "isActivityFull", isActivityFull);
            transaction.update(postReference, "peopleStatus", newPeopleStatus);
            return null;
        });

        try {
            // Ensure transaction completes
            transactionFuture.get();  // Wait for the transaction to complete
            System.out.println("Signed up count incremented successfully for post: " + postId);
        } catch (Exception e) {
            logError("Error incrementing signed up count for post: " + postId, e);
        }
    }
}
