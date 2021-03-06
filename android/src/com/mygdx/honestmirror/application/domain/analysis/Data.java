package com.mygdx.honestmirror.application.domain.analysis;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelMPI.body_part;

//this class provides an interface to the vector data so the analysis
//implementation doesn't depend on the external format.
public interface Data {
    // frame Integer index to a specific frame in the data structure.
    // bp Body part (also used as index) you want the coordinate for.
    // A 2 component integer vector that contains the specified body part's coordinate in indexed frames' screen space.
    Vector3 getCoord(long frame, body_part bp);

    //Sets the X component of a specific coordinate of a body part and frame.
    void setX(long frame, body_part bp, double x);

    //Sets the Y component of a specific coordinate of a body part and frame.
    void setY(long frame, body_part bp, double  newComponentValue_Y);

    // The number of body parts used in the data structure.
    int getBodyPartCount();

    // The number of total frames in the videos' data structure.
    long getFrameCount();

    //The number of frames per second of the original video.
    float getFps();

}