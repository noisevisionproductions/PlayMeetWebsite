package com.noisevisionproduction.playmeetwebsite.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostsRegistrationService extends LogsPrint {

    private final Firestore firestore;

    @Autowired
    public PostsRegistrationService(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<Map<String, Object>> getRegistrationsForPost(String postId) {
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
}
