package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import com.google.firebase.database.*;
import com.noisevisionproduction.playmeetwebsite.firebase.PostRepository;
import com.noisevisionproduction.playmeetwebsite.userManagement.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostModel> getAllPosts() throws InterruptedException, ExecutionException {
        //return postRepository.getPosts();
        List<PostModel> posts = postRepository.getPosts();
        for (PostModel postModel : posts) {
            UserModel userModel = getUserById(postModel.getUserId()).join();
            if (userModel != null) {
                postModel.setAvatar(userModel.getAvatar());
            }
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
