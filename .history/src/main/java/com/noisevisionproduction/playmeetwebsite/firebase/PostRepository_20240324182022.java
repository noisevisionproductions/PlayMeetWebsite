package com.noisevisionproduction.playmeetwebsite.firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.noisevisionproduction.playmeetwebsite.PostsManagement.PostModel;

@Service
public class PostRepository {

    public List<PostModel> getPosts() throws InterruptedException, ExecutionException {
        Firestore firestoreDB = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestoreDB.collection("PostCreating").get();

        List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();
        List<PostModel> postModels = new ArrayList<>();
        for (QueryDocumentSnapshot documents : documentSnapshots) {
            postModels.add(documents.toObject(PostModel.class));
        }
        return postModels;
    }
}
