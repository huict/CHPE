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

    public void setValues(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
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
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            long startTime = System.nanoTime();

            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;
                Person p = pnh.estimateSinglePose(mp);
                persons.add(p);
                long endTime = System.nanoTime();
                DebugLog.log("Thread 1's singular person creation took " + (endTime - startTime) / 1000000 + " ms");
                sleep(10);

            } catch (IllegalStateException | InterruptedException ise) {
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

    public void setValues(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
        this.timeProcessed = (long) (totalVideoTime * 0.25 * 1000);
        this.totalVideoTime = (long) (totalVideoTime * 0.5 * 1000);
        this.pnh = pnh;
        this.mediaMetadataRetriever = mediaMetadataRetriever;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //thread that takes from 25% to 50% of the video
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void start(){
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;

                Person p = pnh.estimateSinglePose(mp);
                persons.add(p);
                sleep(10);

            } catch (IllegalStateException | InterruptedException ise) {
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

    public void setValues(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
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
    public void start(){
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;
                Person p = pnh.estimateSinglePose(mp);
                persons.add(p);
                sleep(10);

            } catch (IllegalStateException | InterruptedException ise) {
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

    public void setValues(long totalVideoTime, PoseNetHandler pnh, MediaMetadataRetriever mediaMetadataRetriever) {
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
    public void start(){
        while(this.timeProcessed + 1 <= this.totalVideoTime){
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;
                Person p = pnh.estimateSinglePose(mp);
                persons.add(p);
                sleep(10);

            } catch (IllegalStateException | InterruptedException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
        }
    }
}
