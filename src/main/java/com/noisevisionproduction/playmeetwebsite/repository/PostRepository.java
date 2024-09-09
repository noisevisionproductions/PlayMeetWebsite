package com.noisevisionproduction.playmeetwebsite.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class PostRepository {

    private final Firestore firestore;

    public PostRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public String savePost(PostModel postModel) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection("PostCreating").add(postModel);
        DocumentReference documentReference = future.get();
        String postId = documentReference.getId();
        postModel.setPostId(postId);
        documentReference.set(postModel);
        return postId;
    }
}
