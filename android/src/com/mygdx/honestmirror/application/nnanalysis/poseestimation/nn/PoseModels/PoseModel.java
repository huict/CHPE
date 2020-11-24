package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels;

/**
 * The interface Pose model.
 */
public interface PoseModel {
    /**
     * Gets model.
     *
     * @return the model
     */
    String getModel();

    /**
     * Gets protocol buffer.
     *
     * @return the protocol buffer
     */
    String getProtocolBuffer();

    /**
     * Get pose pairs int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    int[][] getPosePairs();

    /**
     * Gets points.
     *
     * @return the points
     */
    int getPoints();

    /**
     * Get body parts string [ ].
     *
     * @return the string [ ]
     */
    String[] getBodyParts();


}
