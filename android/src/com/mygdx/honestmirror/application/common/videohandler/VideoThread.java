package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

class BitmapThread extends Thread {

    private final BlockingQueue<Integer> blockingQueue;
    List<Bitmap> bitmaps = new ArrayList<>();
    private final MediaMetadataRetriever mediaMetadataRetriever;

    public BitmapThread(MediaMetadataRetriever mediaMetadataRetriever, BlockingQueue<Integer> blockingQueue) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
        this.blockingQueue = blockingQueue;
    }

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        while(blockingQueue.size() != 0){
            try {
                bitmaps.add(this.mediaMetadataRetriever.getFrameAtIndex(blockingQueue.take()));
                DebugLog.log("Successful, " + blockingQueue.size() + " remaining");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class AnalyseThread extends Thread {
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    BlockingQueue<Bitmap> bitmapQueue;

    public AnalyseThread(BlockingQueue<Bitmap> queue, PoseNetHandler pnh) {
        this.bitmapQueue = queue;
        this.pnh = pnh;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void run(){
        while(bitmapQueue.size() != 0) {
            try {

                Bitmap bitmap = bitmapQueue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                DebugLog.log("Successful, " + bitmapQueue.size() + " remaining");
                persons.add(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}