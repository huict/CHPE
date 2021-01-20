package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;

import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;

import java.util.List;

/**
 * The interface Video splicer.
 */
public interface VideoSplicer {


    /**
     * Is next frame available boolean.
     *
     * @return the boolean
     */
    boolean isNextFrameAvailable();

    boolean isNextTimeAvailable();

    /**
     * Gets frame count.
     *
     * @return the frame count
     */
    long getFrameCount();

    /**
     * Gets next frame.
     *
     * @param frame the frame
     * @return the next frame
     */
    Bitmap getNextFrame(int frame);

    /**
     * Gets next frame.
     *
     * @return the next frame
     * @throws InvalidFrameAccess the invalid frame access
     */
    Bitmap getNextFrame() throws InvalidFrameAccess;

    List<Person> performAnalyse(PoseNetHandler pnh);
}
