package com.noisevisionproduction.playmeetwebsite.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FileStorageService {

    @Value("${firebase.storageBucket}")
    private String bucketName;
    private final FirebaseApp firebaseApp;

    @Autowired
    public FileStorageService(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    public String uploadAvatarToFirebase(MultipartFile file, String userId) throws IOException {
        Bucket bucket = StorageClient.getInstance(firebaseApp).bucket(bucketName);

        String fileName = "avatars/" + userId;

        bucket.create(fileName, file.getInputStream(), file.getContentType());

        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName,
                URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        );
    }

    public void deleteAvatarFromFirebase(String userId) throws IOException {
        Bucket bucket = StorageClient.getInstance(firebaseApp).bucket(bucketName);
        String fileName = "avatars/" + userId;
        Blob blob = bucket.get(fileName);
        if (blob != null) {
            blob.delete();
        } else {
            throw new FileNotFoundException("Avatar file not found in Firebase Storage.");
        }
    }
}
