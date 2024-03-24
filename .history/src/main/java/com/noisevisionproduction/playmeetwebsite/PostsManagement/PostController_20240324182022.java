package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noisevisionproduction.playmeetwebsite.firebase.PostRepository;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<PostModel> getPosts() throws InterruptedException, ExecutionException {
        return postRepository.getPosts();
    }
}
