package com.mygdx.honestmirror.application.domain.feedback;

/**
 * hold the infomration found about a pose in a frame
 */
public class PoseData {
    private final EstimatedPose pose;
    private final float timeMilliseconds;

    public PoseData(EstimatedPose pose, float timeMilliseconds) {
        this.pose = pose;
        this.timeMilliseconds = timeMilliseconds;
    }

    public EstimatedPose getPose() {
        return pose;
    }

    public float getTimeMilliseconds() {
        return timeMilliseconds;
    }

    @Override
    public String toString() {
        return "PoseData{" +
                "pose=" + pose +
                ", timeMilliseconds=" + timeMilliseconds +
                '}';
    }
}
