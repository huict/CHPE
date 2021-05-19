package com.mygdx.honestmirror.application.common.videohandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mygdx.honestmirror.application.common.DebugLog;
import com.mygdx.honestmirror.application.common.exceptions.InvalidFrameAccess;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.Person;
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet.PoseNetHandler;
import com.mygdx.honestmirror.view.ui.a_Loading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

/**
 * The type Video splicer.
 */
@SuppressWarnings("CallToThreadRun")
public class VideoSplicerUri implements VideoSplicer {
    private static final String TAG = VideoSplicerUri.class.getSimpleName();
    private static final int META_VIDEO_FRAME_COUNT = 24;
    private static final int META_VIDEO_DURATION = 9;

    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    //The length of the video
    long totalTimeInMs = 0;
    long frameCount = -1;
    int framesProcessed = 0;
    int timeProcessed = 0;

    /**
     * Instantiates a new Video splicer.
     *
     * @param uri the uri
     */
    public VideoSplicerUri(String uri) {

        // Accessing the file
        this.mediaMetadataRetriever.setDataSource(uri);

        // Getting the amount of frames in video
        this.getAmountOfFrames();
    }


    //Instantiates a new Video splicer.
    public VideoSplicerUri(Uri uri, Context context) {

        // Accessing the file
        this.mediaMetadataRetriever.setDataSource(context, uri);

        // Getting the video duration
        this.getVideoDuration();

        // Getting the amount of frames in video
    }

    //Instantiates a new Video splicer uri.
    public VideoSplicerUri(MediaMetadataRetriever retriever){
        this.mediaMetadataRetriever = retriever;
        //DebugLog.log("getVideoDuration " +  this.getVideoDuration());
        this.getVideoDuration();
        this.getAmountOfFrames();
        //DebugLog.log("***getAmountOfFrames*** " +  this.frameCount);
    }

    public long getFrameCount() {
        return this.frameCount;
    }

    long getVideoDuration() throws NumberFormatException {
        try {
            String sTotalTime = this.mediaMetadataRetriever.extractMetadata(META_VIDEO_DURATION);
            this.totalTimeInMs = Long.parseLong(sTotalTime);
            return totalTimeInMs;
        } catch (NumberFormatException nfe) {
            //DebugLog.log("125: Line Exception" + nfe);
            throw new NumberFormatException();
        }
    }

    private void getAmountOfFrames() {
        try {
            String sFrameCount = this.mediaMetadataRetriever.extractMetadata(META_VIDEO_FRAME_COUNT);
            this.frameCount = Integer.parseInt(sFrameCount);
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "NumberFormatException: " + nfe.getMessage());
        }
    }

    public boolean isNextFrameAvailable() {
        return this.framesProcessed + 1 <= this.frameCount;
    }

    public boolean isNextTimeAvailable() {
        return this.timeProcessed + 1 <= (this.totalTimeInMs * 1000 );
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame(int frame) {
        return this.mediaMetadataRetriever.getFrameAtIndex(
                frame);
    }

    @Deprecated
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame() throws InvalidFrameAccess {

        if (this.frameCount <= 0) {this.getAmountOfFrames();}

        if (isNextTimeAvailable()) {
            Bitmap mp;
            try {
                mp = this.mediaMetadataRetriever.getScaledFrameAtTime(this.timeProcessed, 0, 257, 257);
                //per 24 frames
                this.timeProcessed += 400000;

            } catch (IllegalStateException ise) {
                mp = Bitmap.createBitmap(257, 257, Bitmap.Config.ALPHA_8);
            }
            return mp;
        }
        throw new InvalidFrameAccess("InvalidFrameAccess", new Throwable("Next Frame doesn't exist."));
    }

    //the main function that handles the assignment of analysis and threading
    //with defined video, obtain frames and perform Tensorflow per frame for results
    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<Person> performAnalyse(PoseNetHandler pnh) {

        //DebugLog.log("currently on: "+ Thread.currentThread().getName());
        long startTime = System.nanoTime();
        //create a queue to take all the frames you want to get (frame 0, frame 3, frame 6 etc)
        //24 frames per second makes 2.5 seconds per frame
        BlockingQueue<Integer> integerQueue = new LinkedBlockingDeque<>();
        DebugLog.log("totalTimeInMs = " + this.totalTimeInMs*1000);
        getAmountOfFrames();
        DebugLog.log("framecount = " + this.frameCount);
        //andriod function needs timestamps in nanoseconds!
        int numberOfframes = 0;
        for(int i = 0; i < totalTimeInMs * 1000; i+= (41666*3)){
            integerQueue.add(i);
            numberOfframes += 1;
        }

        a_Loading proggresbar = ((a_Loading)getApplicationContext());
        proggresbar.setProgressBar(50);

        DebugLog.log("number of frames = " + numberOfframes);

        //get all the bitmaps
        //performs on 3 threads as of writing, Thread 7, 9 and 10.
        //Application crashes if attempting to put the bitmap thread initializer in the for loop
        //error: java.lang.IllegalStateException: No retriever available
        //remove the if/else statement and only one thread is being used
        List<Bitmap> bitmapList = new ArrayList<>();
        BitmapThread bitmapThread = new BitmapThread(this.mediaMetadataRetriever, integerQueue, bitmapList);
        BitmapThread2 bitmapThread2 = new BitmapThread2(this.mediaMetadataRetriever, integerQueue, bitmapList);

        for(int i = 1; i < 4; i++){
            if(i == 1){
                bitmapThread.start();
                bitmapThread2.start();
            }
            else{
                bitmapThread.run();
                bitmapThread2.run();
            }
        }

        while(bitmapThread.isAlive()){
            //DebugLog.log("waiting...");
        }
        this.mediaMetadataRetriever.close();
        //DebugLog.log("BitmapThreads finished, starting analysis!");

        //create queue with all the bitmaps
        BlockingQueue<Bitmap> bitmapQueue = new LinkedBlockingDeque<>(bitmapList);

        //perform analysis
        //analyseThread.run() crashes the application
        //currently only runs on one thread, being 10.5
        AnalyseThread analyseThread = new AnalyseThread(bitmapQueue, pnh);
        for(int i = 1; i < 2; i++){
            //DebugLog.log("AnalyseThread " + i + " starts now");
            try{
                analyseThread.start();
            }
            catch (IllegalThreadStateException e){
                //DebugLog.log("Further Threads have not been made");
            }
        }

        long endTime = System.nanoTime();
        DebugLog.log("full analysis took: " + (endTime - startTime) / 1000000000 + " Seconds");

        //receive all persons
        return analyseThread.getPersons();
    }
}
