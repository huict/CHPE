package com.mygdx.honestmirror.application.domain.feedback.settings;

import com.mygdx.honestmirror.application.domain.feedback.EstimatedPose;

/**
 * settings for the poses
 */
public class FeedbackSetting {
    private final EstimatedPose pose;
    private final int maxPersistSeconds;
    private final OccurrenceOverTime maxOccurrence;

    /**
     * sets the settings for the a specific poses
     * @param pose pose the settings are for
     * @param maxPersistSeconds the max amount of seconds a pose can persist
     * @param maxOccurrence the max amount of times a pose should happen during a presentation
     */
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
