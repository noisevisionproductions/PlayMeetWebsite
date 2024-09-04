package com.noisevisionproduction.playmeetwebsite.firebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.noisevisionproduction.playmeetwebsite.PostsManagement.PostModel;

/**
 * Class is responsible for access to posts informations from Firestore.
 *
 * @Service annotation indicates that this class is service component in Spring,
 * which will be automatically
 * detected during path scanning and creates from them Beans
 * (singletones), which can be injected as dependencies
 * in other parts of the Spring application.
 */
@Service
public class PostRepository extends LogsPrint {

    private final Firestore firestore;

    @Autowired
    public PostRepository(Firestore firestore) {
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
}
