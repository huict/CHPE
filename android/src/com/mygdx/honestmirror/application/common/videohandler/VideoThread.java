package com.mygdx.honestmirror.application.common.videohandler;

import android.graphics.Bitmap;
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

    public void start(){
        while(queue != null) {
            try {
                Bitmap bitmap = queue.take();
                Person p = pnh.estimateSinglePose(bitmap);
                persons.add(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}