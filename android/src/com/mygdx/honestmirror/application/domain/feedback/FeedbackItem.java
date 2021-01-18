package com.mygdx.honestmirror.application.domain.feedback;

public class FeedbackItem {
    private final EstimatedPose estimatedPose;
    private final String name;
    private final String description;

    public FeedbackItem(EstimatedPose estimatedPose, String name, String description) {
        this.estimatedPose = estimatedPose;
        this.name = name;
        this.description = description;
    }

    public EstimatedPose getEstimatedPose() {
        return estimatedPose;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFeedback() {
       return name;
    }


    public String getShortFeedback() {
        return description;
    }
}
