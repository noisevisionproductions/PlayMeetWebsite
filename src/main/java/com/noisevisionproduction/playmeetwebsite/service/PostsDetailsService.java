package com.noisevisionproduction.playmeetwebsite.service;

import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostsDetailsService extends LogsPrint {

    private final PostsService postsService;
    private final PostsRegistrationService postsRegistrationService;
    private final UserService userService;

    @Autowired
    public PostsDetailsService(PostsService postsService, PostsRegistrationService postsRegistrationService, UserService userService) {
        this.postsService = postsService;
        this.postsRegistrationService = postsRegistrationService;
        this.userService = userService;
    }

    public List<PostModel> getAllPosts() throws InterruptedException, ExecutionException {
        List<PostModel> posts = postsService.getPosts();

        for (PostModel postModel : posts) {
            postWithUserDetails(postModel);
            registrationsOfThePosts(postModel);
        }
        return posts;
    }

    // New method to get posts created by a specific user
    public List<PostModel> getUserPosts(String userId) throws InterruptedException, ExecutionException {
        List<PostModel> posts = postsService.getPostsByUserId(userId);

        for (PostModel postModel : posts) {
            postWithUserDetails(postModel);
            registrationsOfThePosts(postModel);
        }
        return posts;
    }

    private void postWithUserDetails(PostModel postModel) throws InterruptedException, ExecutionException {
        CompletableFuture<UserModel> userFuture = getUserInformationByHisId(postModel.getUserId());
        UserModel userModel = userFuture.get();
        if (userModel != null) {
            postModel.setAvatar(userModel.getAvatar());
        }
    }

    private void registrationsOfThePosts(PostModel postModel) {
        List<Map<String, Object>> registrations = postsRegistrationService.getRegistrationsForPost(postModel.getPostId());
        List<UserModel> signedUpUsers = registrations.stream()
                .map(registration -> (String) registration.get("userId"))
                .filter(Objects::nonNull)
                .map(userId -> userService.getUserById(userId).join())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        postModel.setRegistrations(signedUpUsers);
    }

    private CompletableFuture<UserModel> getUserInformationByHisId(String userId) {
        return userService.getUserById(userId);
    }
}
