package com.noisevisionproduction.playmeetwebsite.service;

import com.noisevisionproduction.playmeetwebsite.model.PostModel;
import com.noisevisionproduction.playmeetwebsite.model.PostRequest;
import com.noisevisionproduction.playmeetwebsite.repository.PostRepository;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCreatingService extends LogsPrint {

    private final PostRepository postRepository;

    @Autowired
    public PostCreatingService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public String createPost(PostRequest postRequest, String userId) {
        PostModel postModel = new PostModel();
        postModel.setSportType(postRequest.getSportName());
        postModel.setCityName(postRequest.getCityName());
        postModel.setHowManyPeopleNeeded(postRequest.getNumberOfPeople());

        if (postRequest.isNoDateTime()) {
            postModel.setDateTime("Data do uzgodnienia");
        } else {
            postModel.setDateTime(postRequest.getDateTime());
        }
        if (postRequest.isNoHourTime()) {
            postModel.setHourTime("Godzina do uzgodnienia");
        } else {
            postModel.setHourTime(postRequest.getHourTime());
        }

        postModel.setSkillLevel(postRequest.getSkillLevel());

        if (postRequest.getAdditionalInfo().isEmpty()) {
            postModel.setAdditionalInfo("Nie podano dodatkowych informacji.");
        } else {
            postModel.setAdditionalInfo(postRequest.getAdditionalInfo());
        }

        postModel.setUserId(userId);

        try {
            return postRepository.savePost(postModel);
        } catch (Exception e) {
            logError("Error saving post in Firestore ", e);
            return null;
        }
    }
}
