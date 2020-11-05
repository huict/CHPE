package com.mygdx.game.PoseEstimation;

import android.graphics.Bitmap;
import android.util.Log;

import com.mygdx.game.DebugLog;
import com.mygdx.game.Exceptions.InvalidFrameAccess;
import com.mygdx.game.PoseEstimation.nn.PoseNet.Person;
import com.mygdx.game.PoseEstimation.nn.PoseNet.PoseNetHandler;
import com.mygdx.game.VideoHandler.VideoSplicer;

class CreateBitmapThread extends Thread {
    public VideoSplicer videoSplicer;
    Bitmap bitmap;

    public CreateBitmapThread(VideoSplicer videoSplicer){
        this.videoSplicer = videoSplicer;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void start() {
        try {
            // 150-200 ms
            long newStartTime = System.nanoTime();

            bitmap = videoSplicer.getNextFrame();

            long newEndTime = System.nanoTime();
            DebugLog.log("create next frame Took: " + ((newEndTime - newStartTime) / 1000000) + "ms");
        } catch (InvalidFrameAccess invalidFrameAccess) {
            Log.e("runVideo -> PoseNet - Iterator", "runVideo: ", invalidFrameAccess);
        }
    }
}

class EstimatePoseThread extends Thread {
    Person person = null;
    PoseNetHandler pnh;
    Bitmap bitmap;

    public EstimatePoseThread(PoseNetHandler pnh, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.pnh = pnh;
    }

    public Person getPerson() {
        return this.person;
    }

    public void start(){
        try{
            long totalstartTime = System.nanoTime();

            this.person = pnh.estimateSinglePose(this.bitmap);

            long estimateEndTime = System.nanoTime();
            DebugLog.log("estimate single pose Took: " + ((estimateEndTime - totalstartTime) / 1000000) + "ms");
        }
        catch(Exception e){
            DebugLog.log("something went wrong with thread 2");
        }
    }
}