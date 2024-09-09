package com.noisevisionproduction.playmeetwebsite.model;

// Class that captures data from the frontend
public class PostRequest {

    private String sportName;
    private String cityName;
    private int skillLevel;
    private int numberOfPeople;
    private String dateTime;
    private boolean noDateTime;
    private String hourTime;
    private boolean noHourTime;
    private String additionalInfo;

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isNoDateTime() {
        return noDateTime;
    }

    public void setNoDateTime(boolean noDateTime) {
        this.noDateTime = noDateTime;
    }

    public String getHourTime() {
        return hourTime;
    }

    public void setHourTime(String hourTime) {
        this.hourTime = hourTime;
    }

    public boolean isNoHourTime() {
        return noHourTime;
    }

    public void setNoHourTime(boolean noHourTime) {
        this.noHourTime = noHourTime;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
