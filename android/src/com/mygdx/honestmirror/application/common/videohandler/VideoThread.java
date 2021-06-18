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

import static com.mygdx.honestmirror.GlobalApplication.addToProgressBar;

class BitmapThread extends Thread {

    private final BlockingQueue<Integer> integerQueue;
    List<Bitmap> bitmaps;
    private final MediaMetadataRetriever mediaMetadataRetriever;
    float progression;
    int amountProgressed;

    public BitmapThread(MediaMetadataRetriever mediaMetadataRetriever, BlockingQueue<Integer> blockingQueue, List<Bitmap> bitmapList) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
        this.integerQueue = blockingQueue;
        this.bitmaps = bitmapList;
        this.progression = (float) integerQueue.size()/100;
        DebugLog.log("amount " + progression);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        while(integerQueue.size() != 0){
            try {
                if(amountProgressed >= progression)
                {

                    amountProgressed = 0;
                    addToProgressBar(25);

                }
                long startTime = System.nanoTime();
                int i = integerQueue.take();
                Bitmap bitmap = (this.mediaMetadataRetriever.getScaledFrameAtTime(i,0,257,257));
           //     Bitmap bitmap = (this.mediaMetadataRetriever.getFrameAtIndex(i));
                bitmaps.add(bitmap);
                long endTime = System.nanoTime();
                amountProgressed = amountProgressed + 1;
                Thread.sleep(1);
                //DebugLog.log("run BitmapThread: Successful in "+ ((endTime-startTime) / 1000000) + "ms in thread " + Thread.currentThread().getName() + ", " + integerQueue.size() + " remaining");
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
    float progression;
    int amountProgressed;

    public BitmapThread2(MediaMetadataRetriever mediaMetadataRetriever, BlockingQueue<Integer> blockingQueue, List<Bitmap> bitmapList) {
        this.mediaMetadataRetriever = mediaMetadataRetriever;
        this.integerQueue = blockingQueue;
        this.bitmaps = bitmapList;
        this.progression = (float) integerQueue.size()/100;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        while(integerQueue.size() != 0){

            try {
                if(amountProgressed >= progression)
                {
                    amountProgressed = 0;
                    addToProgressBar(25);

                }
                long startTime = System.nanoTime();
                int i = integerQueue.take();
                Bitmap bitmap = (this.mediaMetadataRetriever.getScaledFrameAtTime(i,0,257,257));
        //        Bitmap bitmap = (this.mediaMetadataRetriever.getFrameAtIndex(i));
                bitmaps.add(bitmap);
                long endTime = System.nanoTime();
                //this.interrupt();
                amountProgressed = amountProgressed + 1;
                Thread.sleep(1);
              //  DebugLog.log("run BitmapThread2: Successful in "+ ((endTime-startTime) / 1000000) + "ms in thread " + Thread.currentThread().getName() + ", " + integerQueue.size() + " remaining");
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
    float progression;
    int amountProgressed;

    public AnalyseThread(BlockingQueue<Bitmap> queue, PoseNetHandler pnh) {
        this.bitmapQueue = queue;
        this.pnh = pnh;
        this.progression = (float) bitmapQueue.size()/100;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void start(){
        while(bitmapQueue.size() != 0) {
            try {
                if(amountProgressed >= progression)
                {
                    amountProgressed = 0;
                    addToProgressBar(100);

                }
                Bitmap bitmap = bitmapQueue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                persons.add(p);
                //DebugLog.log("start Successful in Thread" + Thread.currentThread().getName() + ", " + bitmapQueue.size() + " remaining");
                amountProgressed = amountProgressed + 1;
                Thread.sleep(1);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}