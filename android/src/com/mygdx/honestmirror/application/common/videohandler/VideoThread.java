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

    private final BlockingQueue<Integer> integerQueue;
    List<Bitmap> bitmaps;
    private final MediaMetadataRetriever mediaMetadataRetriever;

    public BitmapThread(MediaMetadataRetriever mediaMetadataRetriever, BlockingQueue<Integer> blockingQueue, List<Bitmap> bitmapList) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
        this.integerQueue = blockingQueue;
        this.bitmaps = bitmapList;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        while(integerQueue.size() != 0){
            try {
                long startTime = System.nanoTime();
                int i = integerQueue.take();
                Bitmap bitmap = (this.mediaMetadataRetriever.getScaledFrameAtTime(i,0,257,257));
           //     Bitmap bitmap = (this.mediaMetadataRetriever.getFrameAtIndex(i));
                bitmaps.add(bitmap);
                long endTime = System.nanoTime();
                DebugLog.log("run BitmapThread: Successful in "+ ((endTime-startTime) / 1000000) + "ms in thread " + Thread.currentThread().getName() + ", " + integerQueue.size() + " remaining");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BitmapThread2 extends Thread {

    private final BlockingQueue<Integer> integerQueue;
    List<Bitmap> bitmaps;
    private final MediaMetadataRetriever mediaMetadataRetriever;

    public BitmapThread2(MediaMetadataRetriever mediaMetadataRetriever, BlockingQueue<Integer> blockingQueue, List<Bitmap> bitmapList) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
        this.integerQueue = blockingQueue;
        this.bitmaps = bitmapList;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        while(integerQueue.size() != 0){

            try {
                long startTime = System.nanoTime();
                int i = integerQueue.take();
                Bitmap bitmap = (this.mediaMetadataRetriever.getScaledFrameAtTime(i,0,257,257));
        //        Bitmap bitmap = (this.mediaMetadataRetriever.getFrameAtIndex(i));
                bitmaps.add(bitmap);
                long endTime = System.nanoTime();
                DebugLog.log("run BitmapThread2: Successful in "+ ((endTime-startTime) / 1000000) + "ms in thread " + Thread.currentThread().getName() + ", " + integerQueue.size() + " remaining");
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

    public void start(){
        while(bitmapQueue.size() != 0) {
            try {

                Bitmap bitmap = bitmapQueue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                persons.add(p);
                DebugLog.log("start Successful in Thread" + Thread.currentThread().getName() + ", " + bitmapQueue.size() + " remaining");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}