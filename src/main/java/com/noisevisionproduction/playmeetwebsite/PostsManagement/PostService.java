package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import com.noisevisionproduction.playmeetwebsite.firebase.PostRepository;
import com.noisevisionproduction.playmeetwebsite.userManagement.UserModel;
import com.noisevisionproduction.playmeetwebsite.userManagement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostService extends LogsPrint {

    private final PostRepository postRepository;

    private final RegistrationService registrationService;

    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, RegistrationService registrationService, UserService userService) {
        this.postRepository = postRepository;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    public List<PostModel> getAllPosts() throws InterruptedException, ExecutionException {
        List<PostModel> posts = postRepository.getPosts();

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
        List<Map<String, Object>> registrations = registrationService.getRegistrationsForPost(postModel.getPostId());
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
