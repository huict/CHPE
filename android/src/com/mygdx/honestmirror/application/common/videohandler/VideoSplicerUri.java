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

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The type Video splicer.
 */
@SuppressWarnings("CallToThreadRun")
public class VideoSplicerUri implements VideoSplicer {
    private static final String TAG = VideoSplicerUri.class.getSimpleName();
    private static final int META_VIDEO_FRAME_COUNT = 32;
    private static final int META_VIDEO_DURATION = 9;

    /**
     * The Media metadata retriever.
     */
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    /**
     * The length of the video
     */
    long totalTimeInMs = 0;
    /**
     * The Frame count.
     */
    long frameCount = -1;
    /**
     * The Frames processed.
     */
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


    /**
     * Instantiates a new Video splicer.
     *
     * @param uri     the uri
     * @param context the context
     */
    public VideoSplicerUri(Uri uri, Context context) {

        // Accessing the file
        this.mediaMetadataRetriever.setDataSource(context, uri);

        // Getting the video duration
        this.getVideoDuration();

        // Getting the amount of frames in video
    }

    /**
     * Instantiates a new Video splicer uri.
     *
     * @param retriever the retriever
     */
    public VideoSplicerUri(MediaMetadataRetriever retriever){
        this.mediaMetadataRetriever = retriever;

        this.getVideoDuration();
        this.getAmountOfFrames();
    }


    /**
     * Gets frame count.
     *
     * @return the frame count
     */
    public long getFrameCount() {
        return this.frameCount;
    }

    /**
     * Gets frames processed.
     *
     * @return the frames processed
     */
    public long getFramesProcessed() {
        return framesProcessed;
    }

    /**
     * Gets video duration.
     *
     * @return the video duration
     * @throws NumberFormatException the number format exception
     */
    long getVideoDuration() throws NumberFormatException {
        try {
            String sTotalTime = this.mediaMetadataRetriever.extractMetadata(META_VIDEO_DURATION);
            this.totalTimeInMs = Long.parseLong(sTotalTime);
            return totalTimeInMs;
        } catch (NumberFormatException nfe) {
            DebugLog.log("125: Line Exception" + nfe);
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

    /**
     * Is next frame available boolean.
     *
     * @return the boolean
     */
    public boolean isNextFrameAvailable() {
        return this.framesProcessed + 1 <= this.frameCount;
    }

    public boolean isNextTimeAvailable() {
        return this.timeProcessed + 1 <= (this.totalTimeInMs * 1000 );
    }

    /**
     * Gets next frame.
     *
     * @param frame the frame
     * @return the next frame
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getNextFrame(int frame) {
        return this.mediaMetadataRetriever.getFrameAtIndex(
                frame);
    }

    /**
     * Gets next frame.
     *
     * @return the next frame
     * @throws InvalidFrameAccess the invalid frame access
     */
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    //40 seconds for 11 seconds video
    public List<Person> performAnalyse(PoseNetHandler pnh) {

        DebugLog.log("currently on: "+ Thread.currentThread());
        long startTime = System.nanoTime();
        //create a queue to take all the frames you want to get (frame 0, frame 3, frame 6 etc)
        //24 frames per second makes 2.5 seconds per frame
        BlockingQueue<Integer> integerQueue = new LinkedBlockingDeque<>();
        for(int i = 0; i < totalTimeInMs; i+= 67){
            integerQueue.add(i);
        }

        //get all the bitmaps
        //performs on 2 threads as of writing, Thread 7.5 and Thread 9.5.
        //Third thread is only called after the first two threads are done running
        //Application crashes if attempting to put the bitmap thread initializer in the for loop
        //error: java.lang.IllegalStateException: No retriever available
        BitmapThread bitmapThread = new BitmapThread(this.mediaMetadataRetriever, integerQueue);
        for(int i = 1; i < 4; i++){
            DebugLog.log("BitmapThread " + i + " starts now");
            if(i == 1){
                bitmapThread.start();
            }
            else{
                bitmapThread.run();
            }
        }

        this.mediaMetadataRetriever.close();
        if (bitmapThread.isAlive()){
            DebugLog.log("waiting...");
        }
        DebugLog.log("BitmapThreads finished, starting analysis!");
        //create queue with all the bitmaps
        BlockingQueue<Bitmap> bitmapQueue = new LinkedBlockingDeque<>(bitmapThread.getBitmaps());

        //perform analysis
        //analyseThread.run() crashes the application
        //currently only runs on one thread, being 10.5
        AnalyseThread analyseThread = new AnalyseThread(bitmapQueue, pnh);
        for(int i = 1; i < 2; i++){
            DebugLog.log("AnalyseThread " + i + " starts now");
            try{
                analyseThread.start();
            }
            catch (IllegalThreadStateException e){
                DebugLog.log("Further Threads have not been made");
            }
        }

        long endTime = System.nanoTime();
        DebugLog.log("full analysis took: " + (endTime - startTime) / 1000000000 + " Seconds");
        //receive all persons
        return analyseThread.getPersons();
    }
}
