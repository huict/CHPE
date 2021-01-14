package com.mygdx.honestmirror.application.domain.feedback;

public class FeedbackItem {
    private EstimatedPose estimatedPose;
    private String name;
    private String description;

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
}
