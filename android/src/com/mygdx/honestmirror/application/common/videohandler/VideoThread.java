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

class ThreadWithQueue extends Thread {
    private int threadAmount;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private BlockingQueue frameQueue;

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public ThreadWithQueue(int threadAmount, MediaMetadataRetriever mediaMetadataRetriever) {
        this.threadAmount = threadAmount;
        this.mediaMetadataRetriever = mediaMetadataRetriever;

        Bitmap bitmap = this.mediaMetadataRetriever.getScaledFrameAtTime(1, 0, 257, 257);
    }
}

class Thread1 extends Thread {
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    BlockingQueue<Bitmap> queue;

    public Thread1(BlockingQueue<Bitmap> queue, PoseNetHandler pnh) {
        this.queue = queue;
        this.pnh = pnh;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void start(){
        while(queue.size() != 0) {
            try {
                Bitmap bitmap = queue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                persons.add(p);
                DebugLog.log("success");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}