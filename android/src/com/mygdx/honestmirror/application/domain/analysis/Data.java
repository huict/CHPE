package com.mygdx.honestmirror.application.domain.analysis;

import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI.body_part;
import com.badlogic.gdx.math.Vector3;

/**
 * this class provides an interface to the vector data so the analysis
 * implementation doesn't depend on the external format.
 * @author Nico van Bentum
 */
public interface Data {
    /**
     *
     * @param frame Integer index to a specific frame in the data structure.
     * @param bp Body part (also used as index) you want the coordinate for.
     * @return A 2 component integer vector that contains the specified body part's coordinate
     * in indexed frames' screen space.
     */
    Vector3 getCoord(long frame, body_part bp);

    /**
     * Sets the X component of a specific coordinate of a body part and frame.
     * @param frame Frame index.
     * @param bp Body part.
     * @param x new component value.
     */
    void setX(long frame, body_part bp, double x);

    /**
     * Sets the Y component of a specific coordinate of a body part and frame.
     * @param frame Frame index.
     * @param bp Body part.
     * @param y new component value.
     */
    void setY(long frame, body_part bp, double  y);

    /**
     *
     * @return The number of body parts used in the data structure.
     */
    int getBodyPartCount();

    /**
     *
     * @return The number of total frames in the videos' data structure.
     */
    long getFrameCount();

    /**
     *
     * @return The number of frames per second of the original video.
     */
    float getFps();

    /**
     * Serializes the processed data back to the data structure object.
     * TODO: WIP, this might end up being split into multiple functions
     */
    void serialize();

}