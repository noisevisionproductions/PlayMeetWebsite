package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import com.google.firebase.database.*;
import com.noisevisionproduction.playmeetwebsite.firebase.PostRepository;
import com.noisevisionproduction.playmeetwebsite.userManagement.UserModel;
import com.noisevisionproduction.playmeetwebsite.userManagement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    public List<PostModel> getAllPostsWithRegistrationsAndUsers() throws InterruptedException, ExecutionException {
        List<PostModel> posts = postRepository.getPosts();
        for (PostModel postModel : posts) {
            // Pobierz dane użytkownika, który stworzył post
            UserModel userModel = getUserById(postModel.getUserId()).join();
            if (userModel != null) {
                postModel.setAvatar(userModel.getAvatar());  // Ustawienie avatara dla twórcy posta
            }

            // Pobierz zapisanych użytkowników
            List<Map<String, Object>> registrations = registrationService.getRegistrationsForPost(postModel.getPostId());
            List<UserModel> signedUpUsers = new ArrayList<>();
            for (Map<String, Object> registration : registrations) {
                String userId = (String) registration.get("userId");
                if (userId != null) {
                    UserModel signedUpUser = userService.getUserById(userId).join();
                    if (signedUpUser != null) {
                        signedUpUsers.add(signedUpUser);
                    }
                }
            }
            postModel.setRegistrations(signedUpUsers);
        }
        return posts;
    }


    private CompletableFuture<UserModel> getUserById(String userId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("UserModel/" + userId);

        CompletableFuture<UserModel> completableFuture = new CompletableFuture<>();
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                completableFuture.complete(userModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                completableFuture.completeExceptionally(databaseError.toException());
            }
        });
        return completableFuture;
    }
}
