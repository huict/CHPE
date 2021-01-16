package com.mygdx.honestmirror.application.domain.feedback;

public class FeedbackItem {
    private EstimatedPose estimatedPose;
    private String name;
    private String description;
    private int startSeconds = 0;
    private int endSeconds = 0;

    public FeedbackItem(EstimatedPose estimatedPose, String name, String description) {
        this.estimatedPose = estimatedPose;
        this.name = name;
        this.description = description;
    }

    public EstimatedPose getEstimatedPose() {
        return estimatedPose;
    }

    public void setStartSeconds(int startSeconds) {
        this.startSeconds = startSeconds;
    }

    public void setEndSeconds(int endSeconds) {
        this.endSeconds = endSeconds;
    }

    public String getName()
    {
        if (endSeconds != 0){
            return name + " " + getTimeFormat(startSeconds) + " - " + getTimeFormat(endSeconds);
        }

        return name;
    }

    public String getDescription() {
        return description;
    }


    private String getTimeFormat(int seconds){

        int timeSeconds = seconds;
        int timeMinutes = 0;


        while (timeSeconds > 60){
            timeMinutes++;
            timeSeconds -= 60;
        }

        if (timeSeconds < 10)
            return timeMinutes + ":0" + timeSeconds;


        return timeMinutes + ":" + timeSeconds;
    }
}
