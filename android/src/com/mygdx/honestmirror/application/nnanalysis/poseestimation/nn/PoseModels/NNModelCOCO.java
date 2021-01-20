package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels;

/**
 * The type Coco.
 */
public class NNModelCOCO implements PoseModel {
    /**
     * The Protocol buffer.
     */
    public final String protocol_buffer = "pose/coco/pose_deploy_linevec.prototxt";
    /**
     * The Model.
     */
    public final String model = "pose/coco/pose_iter_440000.caffemodel";
    /**
     * The Pose pairs.
     */
    public int[][] POSE_PAIRS =
            {
                    {1, 0},
                    {1, 2},
                    {1, 5},
                    {2, 3},
                    {3, 4},
                    {5, 6},
                    {6, 7},
                    {1, 8},
                    {8, 9},
                    {9, 10},
                    {1, 11},
                    {11, 12},
                    {12, 13},
                    {0, 14},
                    {0, 15},
                    {14, 16},
                    {15, 17}
            };

    /**
     * The Body parts.
     */
    public String[] body_parts = new String[]{
    };

    /**
     * The Points.
     */
    public int points = 18;

    @Override
    public String getModel() {
        return model;
    }

}
