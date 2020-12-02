package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;
import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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

    public void run(){
        while(queue.size() != 0) {
            try {

                Bitmap bitmap = queue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                DebugLog.log("Successful, " + queue.size() + " remaining");
                persons.add(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Thread2 extends Thread {
    List<Person> persons = new ArrayList<>();
    PoseNetHandler pnh;
    BlockingQueue<Bitmap> queue;

    public Thread2(BlockingQueue<Bitmap> queue, PoseNetHandler pnh) {
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