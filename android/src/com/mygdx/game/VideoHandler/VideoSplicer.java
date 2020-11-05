package com.mygdx.game.VideoHandler;

import android.graphics.Bitmap;

import com.mygdx.game.Exceptions.InvalidFrameAccess;

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
     * Gets frames processed.
     *
     * @return the frames processed
     */
    long getFramesProcessed();

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

}
