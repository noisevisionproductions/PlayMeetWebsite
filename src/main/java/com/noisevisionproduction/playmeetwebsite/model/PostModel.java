package com.noisevisionproduction.playmeetwebsite.model;

import java.util.List;

public class PostModel {
    private String postId;
    private Boolean createdByUser = false;
    private Boolean isActivityFull = false;
    private String userId;
    private String avatar;
    private String sportType;
    private String cityName;
    private String dateTime;
    private String hourTime;
    private int skillLevel;
    private String peopleStatus;
    private int howManyPeopleNeeded;
    private String additionalInfo;
    private int signedUpCount = 0;
    private List<UserModel> registrations;

    public int getSignedUpCount() {
        return signedUpCount;
    }

    public void updatePeopleStatus() {
        peopleStatus = getSignedUpCount() + "/" + howManyPeopleNeeded;
    }

    public String getPeopleStatus() {
        return peopleStatus;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        updatePeopleStatus();
        this.postId = postId;
    }

    public Boolean getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Boolean createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Boolean getIsActivityFull() {
        return isActivityFull;
    }

    public void setIsActivityFull(Boolean isActivityFull) {
        this.isActivityFull = isActivityFull;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getHourTime() {
        return hourTime;
    }

    public void setHourTime(String hourTime) {
        this.hourTime = hourTime;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void setPeopleStatus(String peopleStatus) {
        this.peopleStatus = peopleStatus;
    }

    public int getHowManyPeopleNeeded() {
        return howManyPeopleNeeded;
    }

    public void setHowManyPeopleNeeded(int howManyPeopleNeeded) {
        this.howManyPeopleNeeded = howManyPeopleNeeded;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setSignedUpCount(int signedUpCount) {
        this.signedUpCount = signedUpCount;
    }

    public List<UserModel> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<UserModel> registrations) {
        this.registrations = registrations;
    }
}
