package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;

import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;

import java.util.List;

public interface VideoSplicer {

    boolean isNextFrameAvailable();

    boolean isNextTimeAvailable();

    long getFrameCount();

    Bitmap getNextFrame(int frame);

    Bitmap getNextFrame() throws InvalidFrameAccess;

    List<Person> performAnalyse(PoseNetHandler pnh);
}
