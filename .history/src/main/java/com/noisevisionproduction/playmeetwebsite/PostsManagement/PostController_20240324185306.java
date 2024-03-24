package com.noisevisionproduction.playmeetwebsite.PostsManagement;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noisevisionproduction.playmeetwebsite.firebase.PostRepository;

/**
 * Class acts as an API endpoint for posts operations such as downloading the
 * list of available posts. The controller communicates with the repository
 * (PostRepository) to get
 * the data and then returns this data to the client in JSON format.
 * 
 * @RestController annotation means, that this class is a special type of
 *                 controler, which is ready to handle web requests.
 *                 Every single method in the controller serialize their
 *                 responses to JSON and return them in the HTTP response body.
 * 
 * @RequestMapping specifies that all methods handled by this controller will
 *                 have URL paths prefixed with "/api/posts". This is a way to
 *                 group similar operations
 *                 together and make API endpoint magamenemt easier.
 * 
 * @Autowired is annotation, which allows SpringBoot for automatic dependency
 *            injection. Here Spring injects PostRepository instance to the
 *            controller,
 *            and allows for access to methods that are being needed for getting
 *            data about posts.
 * 
 * @GetMapping annotation is a specialization of the @RequestMapping annotation
 *             and
 *             indicates that the getPosts() method should be called in response
 *             to HTTP GET request to the "/api/posts" path. It is a default
 *             way for defining endpoints for data download in RESTful apps.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping(value = "/api/posts", produces = "application/json")
    public List<PostModel> getPosts() throws InterruptedException, ExecutionException {
        return postRepository.getPosts();
    }
}
