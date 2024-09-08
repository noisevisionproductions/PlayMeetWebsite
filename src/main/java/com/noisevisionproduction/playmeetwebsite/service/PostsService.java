package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
}
