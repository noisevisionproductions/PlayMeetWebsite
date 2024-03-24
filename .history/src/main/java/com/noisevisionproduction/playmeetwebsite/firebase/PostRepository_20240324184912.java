package com.noisevisionproduction.playmeetwebsite.firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.noisevisionproduction.playmeetwebsite.PostsManagement.PostModel;

/**
 * Class is responsible for access to posts informations from Firestore.
 * 
 * @Service annotation indicates that this class is service component in Spring,
 *          which will be automatically
 *          detected during path scanning and creates from them Beans
 *          (singletones), which can be injected as dependencies
 *          in other parts of the Spring application.
 */
@Service
public class PostRepository {

    /**
     * Method is responsible for getting posts list from Firestore collection
     * "PostCreating".
     * It uses asynchronic API Firestore for document inquiries, so operations
     * won'be blocked
     * and potentially negatively affects app performance.
     * 
     * @return
     * @throws InterruptedException is thrown if waiting thread for the end of
     *                              the asynchronic process will be interrupted.
     * @throws ExecutionException   is thrown when attempting to retrieve the result
     *                              of a task that aborted by throwing an exception.
     *                              This exception can be inspected using the
     *                              getCause() method.
     */
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
