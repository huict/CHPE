package com.mygdx.honestmirror.application.domain.feedback;

public class PoseData {
    private final EstimatedPose pose;
    private final int timeMilliseconds;

    public PoseData(EstimatedPose pose, int timeMilliseconds) {
        this.pose = pose;
        this.timeMilliseconds = timeMilliseconds;
    }

    public EstimatedPose getPose() {
        return pose;
    }

    public int getTimeMilliseconds() {
        return timeMilliseconds;
    }
}
