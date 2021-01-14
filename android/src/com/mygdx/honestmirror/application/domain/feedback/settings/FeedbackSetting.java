package com.mygdx.honestmirror.application.domain.feedback.settings;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;

public class FeedbackSetting {
    private EstimatedPose pose;
    private int maxPersistSeconds;
    private OccurrenceOverTime maxOccurrence;

    public FeedbackSetting(EstimatedPose pose, int maxPersistSeconds, OccurrenceOverTime maxOccurrence) {
        this.pose = pose;
        this.maxPersistSeconds = maxPersistSeconds;
        this.maxOccurrence = maxOccurrence;
    }

    public EstimatedPose getPose() {
        return pose;
    }

    public int getMaxPersistSeconds() {
        return maxPersistSeconds;
    }

    public OccurrenceOverTime getMaxOccurrence() {
        return maxOccurrence;
    }
}
