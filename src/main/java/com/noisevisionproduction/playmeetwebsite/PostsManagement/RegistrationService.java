package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private Firestore firestore;

    public List<Map<String, Object>> getRegistrationsForPost(String postId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> apiFuture = firestore.collection("registrations")
                .whereEqualTo("postId", postId)
                .get();

        List<QueryDocumentSnapshot> documentSnapshots = apiFuture.get().getDocuments();
        return documentSnapshots.stream()
                .map(QueryDocumentSnapshot::getData)
                .collect(Collectors.toList());
    }
}
