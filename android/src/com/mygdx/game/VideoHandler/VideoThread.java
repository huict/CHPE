package com.mygdx.game.VideoHandler;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.mygdx.game.DebugLog;
import com.mygdx.game.PoseEstimation.nn.PoseNet.Person;
import com.mygdx.game.PoseEstimation.nn.PoseNet.PoseNetHandler;

import java.util.ArrayList;

class Thread1 extends Thread {
    public long totalVideoTime;
    ArrayList<Person> persons;
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public void setTotalVideoTime(long totalVideoTime) {
        this.totalVideoTime = (long) (totalVideoTime * 0.25);
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPnh(PoseNetHandler pnh) {
        this.pnh = pnh;
    }

    //thread that takes the first 25% of the video
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

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
        }
    }
}

class Thread2 extends Thread{
    public long totalVideoTime;
    ArrayList<Person> persons;
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public void setTotalVideoTime(long totalVideoTime) {
        this.timeProcessed = (long) (totalVideoTime * 0.25);
        this.totalVideoTime = (long) (totalVideoTime * 0.5);
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPnh(PoseNetHandler pnh) {
        this.pnh = pnh;
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

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
        }
    }
}

class Thread3 extends Thread{
    public long totalVideoTime;
    ArrayList<Person> persons;
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public void setTotalVideoTime(long totalVideoTime) {
        this.timeProcessed = (long) (totalVideoTime * 0.5);
        this.totalVideoTime = (long) (totalVideoTime * 0.75);
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPnh(PoseNetHandler pnh) {
        this.pnh = pnh;
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

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
        }
    }
}

class Thread4 extends Thread{
    public long totalVideoTime;
    ArrayList<Person> persons;
    PoseNetHandler pnh;
    long timeProcessed;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    public void setTotalVideoTime(long totalVideoTime) {
        this.timeProcessed = (long) (totalVideoTime * 0.75);
        this.totalVideoTime = totalVideoTime;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPnh(PoseNetHandler pnh) {
        this.pnh = pnh;
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

            } catch (IllegalStateException ise) {
                Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
        }
    }
}
