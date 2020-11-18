package com.mygdx.game.VideoHandler;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.mygdx.game.DebugLog;
import com.mygdx.game.PoseEstimation.nn.PoseNet.Person;
import com.mygdx.game.PoseEstimation.nn.PoseNet.PoseNetHandler;
import java.util.ArrayList;
import java.util.List;

class Thread1 extends Thread {
    public long totalVideoTime;
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever;

    public Thread1(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
        this.totalVideoTime = (long) (totalVideoTime * 0.25 * 1000);
        this.pnh = pnh;
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //thread that takes the first 25% of the video
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void start(){
        DebugLog.log("Thread 1 start()");
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            try {
                DebugLog.log("Thread 1: " + Thread.currentThread().getName());
                Bitmap mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                DebugLog.log("Thread 1 bitmap made");
                this.timeProcessed += 41666;
                DebugLog.log("Thread 1 added timeProcessed");
                Person p = pnh.estimateSinglePose(mp);
                DebugLog.log("Thread 1 Person created");
                persons.add(p);
                DebugLog.log("Thread 1 finished");

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);

            }
        }
    }
}

class Thread2 extends Thread{
    public long totalVideoTime;
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever;

    public Thread2(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
        this.timeProcessed = (long) (totalVideoTime * 0.25 * 1000 + 1);
        this.totalVideoTime = (long) (totalVideoTime * 0.5 * 1000);
        this.pnh = pnh;
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //thread that takes from 25% to 50% of the video
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        DebugLog.log("Thread 2 run()");
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            try {
                DebugLog.log("Thread 2: " + Thread.currentThread().getName());
                Bitmap mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                DebugLog.log("Thread 2 bitmap made");
                this.timeProcessed += 41666;
                DebugLog.log("Thread 2 added timeProcessed");
                Person p = pnh.estimateSinglePose(mp);
                DebugLog.log("Thread 2 Person created");
                persons.add(p);
                DebugLog.log("Thread 2 finished");

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);

            }
        }
    }
}

class Thread3 extends Thread{
    public long totalVideoTime;
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever;

    public Thread3(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
        this.timeProcessed = (long) (totalVideoTime * 0.5 * 1000);
        this.totalVideoTime = (long) (totalVideoTime * 0.75 * 1000);
        this.pnh = pnh;
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //thread that takes from 50% to 75% of the video
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        DebugLog.log("Thread 3 run()");
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            try {
                DebugLog.log("Thread 3: " + Thread.currentThread().getName());
                Bitmap mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                DebugLog.log("Thread 3 bitmap made");
                this.timeProcessed += 41666;
                DebugLog.log("Thread 3 added timeProcessed");
                Person p = pnh.estimateSinglePose(mp);
                DebugLog.log("Thread 3 Person created");
                persons.add(p);
                DebugLog.log("Thread 3 finished");

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);

            }
        }
    }
}

class Thread4 extends Thread{
    public long totalVideoTime;
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever;

    public Thread4(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
        this.timeProcessed = (long) (totalVideoTime * 0.75 * 1000);
        this.totalVideoTime = totalVideoTime * 1000;
        this.pnh = pnh;
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //thread that takes the first 75% to 100% of the video
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run(){
        DebugLog.log("Thread 4 run()");
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            try {
                DebugLog.log("Thread 4: " + Thread.currentThread().getName());
                Bitmap mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                DebugLog.log("Thread 4 bitmap made");
                this.timeProcessed += 41666;
                DebugLog.log("Thread 4 added timeProcessed");
                Person p = pnh.estimateSinglePose(mp);
                DebugLog.log("Thread 4 Person created");
                persons.add(p);
                DebugLog.log("Thread 4 finished");

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);

            }
        }
    }
}