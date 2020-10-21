package com.mygdx.game.PoseEstimation.nn.PoseModels;

/**
 * The type Nn model posenet.
 */
public class NNModelPosenet implements PoseModel {

    /**
     * The Pose pairs.
     */
    public final int[][] POSE_PAIRS = {
            {0, 1},
            {1, 2},
            {2, 3},
            {3, 4},
            {1, 5},
            {5, 6},
            {6, 7},
            {8, 9},
            {9, 10},
            {11, 12},
            {12, 13},
            {1, 14},
            {14, 8},
            {14, 11},
    };
    /**
     * The Points.
     */
    public final int points = 17;
    /**
     * The Protocol buffer.
     */
    public final String protocol_buffer = "";
    /**
     * The Model.
     */
    public final String model = "posenet_model.tflite";
    /**
     * The Body parts.
     */
    public String[] bodyParts = new String[]{
            "nose",
            "left_eye",
            "right_eye",
            "left_ear",
            "right_ear",
            "left_shoulder",
            "right_shoulder",
            "left_elbow",
            "right_elbow",
            "left_wrist",
            "right_wrist",
            "left_hip",
            "right_hip",
            "left_knee",
            "right_knee",
            "left_ankle",
            "right_ankle"
    };

    /**
     * The enum Body part.
     */
    public enum bodyPart {
        /**
         * Head body part.
         */
        head,
        /**
         * Neck body part.
         */
        neck,
        /**
         * L shoulder body part.
         */
        l_shoulder,
        /**
         * L elbow body part.
         */
        l_elbow,
        /**
         * L wrist body part.
         */
        l_wrist,
        /**
         * R shoulder body part.
         */
        r_shoulder,
        /**
         * R elbow body part.
         */
        r_elbow,
        /**
         * R wrist body part.
         */
        r_wrist,
        /**
         * L hip body part.
         */
        l_hip,
        /**
         * L knee body part.
         */
        l_knee,
        /**
         * L foot body part.
         */
        l_foot,
        /**
         * R hip body part.
         */
        r_hip,
        /**
         * R knee body part.
         */
        r_knee,
        /**
         * R foot body part.
         */
        r_foot,
        /**
         * Waist body part.
         */
        waist
    }

    @Override
    public int[][] getPosePairs() {
        return POSE_PAIRS;
    }

    @Override
    public int getPoints() {
        return points;
    }

    public String getProtocolBuffer() {
        return protocol_buffer;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String[] getBodyParts() {
        return bodyParts;
    }

}
