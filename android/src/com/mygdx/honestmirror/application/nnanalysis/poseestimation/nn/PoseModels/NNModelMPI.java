package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels;

//The type Mpi.
public class NNModelMPI implements PoseModel {
    //The Pose pairs.
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
    //The Points.
    public int points = 15;
    //The Protocol buffer.
    public final String protocol_buffer = "pose_deploy_linevec_faster_4_stages.prototxt";
    //The Model.
    public final String model = "pose_iter_160000.caffemodel";
    //The Body parts.
    public static String[] body_parts = new String[]{
            "head", "neck", "l_shoulder", "l_elbow",
            "l_wrist", "r_shoulder", "r_elbow",
            "r_wrist", "l_hip", "l_knee", "l_foot",
            "r_hip", "r_knee", "r_foot", "waist"
    };

    //The enum Body part.
    public enum body_part {
        //Head body part.
        head,
        //Neck body part.
        neck,
        //L shoulder body part.
        l_shoulder,
        //L elbow body part.
        l_elbow,
        //L wrist body part.
        l_wrist,
        //R shoulder body part.
        r_shoulder,
        //R elbow body part.

        r_elbow,
        //R wrist body part.
        r_wrist,
        //L hip body part.
        l_hip,
        //L knee body part.
        l_knee,
        //L foot body part.
        l_foot,
        //R hip body part.
        r_hip,
        //R knee body part.
        r_knee,
        //R foot body part.
        r_foot,
        //Waist body part.
        waist
    }

    @Override
    public String getModel() {
        return model;
    }

}
